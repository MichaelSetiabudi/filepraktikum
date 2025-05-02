// Routes for transaction operations
const express = require('express');
const router = express.Router();
const mysql = require('mysql');

// MySQL Connection
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'db_rub'
});

// Get all sales transactions
router.get('/sales', (req, res) => {
  const query = `
    SELECT h.*, c.nama_customer, k.nama_karyawan, p.kode_promo, p.besar_potongan
    FROM h_jual h
    LEFT JOIN customer c ON h.id_customer = c.id_customer
    LEFT JOIN karyawan k ON h.id_karyawan = k.id_karyawan
    LEFT JOIN promo p ON h.kode_promo = p.kode_promo
    ORDER BY h.nota_jual DESC
  `;
  
  db.query(query, (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    res.json(results);
  });
});

// Get a specific sales transaction
router.get('/sales/:id', (req, res) => {
  const salesId = req.params.id;
  
  const headerQuery = `
    SELECT h.*, c.nama_customer, k.nama_karyawan, p.besar_potongan
    FROM h_jual h
    LEFT JOIN customer c ON h.id_customer = c.id_customer
    LEFT JOIN karyawan k ON h.id_karyawan = k.id_karyawan
    LEFT JOIN promo p ON h.kode_promo = p.kode_promo
    WHERE h.nota_jual = ?
  `;
  
  const detailQuery = `
    SELECT d.*, p.nama_produk, p.harga, r.rating
    FROM d_jual d
    INNER JOIN produk p ON d.id_produk = p.id_produk
    LEFT JOIN rating_produk r ON d.nota_jual = r.nota_jual AND d.id_produk = r.id_produk
    WHERE d.nota_jual = ?
  `;
  
  db.query(headerQuery, [salesId], (err, headerResults) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    
    if (headerResults.length === 0) {
      return res.status(404).json({ message: 'Transaction not found' });
    }
    
    db.query(detailQuery, [salesId], (err, detailResults) => {
      if (err) {
        return res.status(500).json({ error: err.message });
      }
      
      res.json({
        header: headerResults[0],
        details: detailResults
      });
    });
  });
});

// Create a new sales transaction
router.post('/sales', (req, res) => {
  const { id_customer, id_karyawan, kode_promo, items } = req.body;
  
  // Validate required fields
  if (!id_customer || !id_karyawan || !items || !items.length) {
    return res.status(400).json({ message: 'Customer ID, Employee ID, and items are required' });
  }
  
  // Generate new transaction ID
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  
  // Get the last transaction number for today
  const datePrefix = `PJ${year}${month}${day}`;
  const getLastIdQuery = `
    SELECT nota_jual
    FROM h_jual
    WHERE nota_jual LIKE '${datePrefix}%'
    ORDER BY nota_jual DESC
    LIMIT 1
  `;
  
  db.query(getLastIdQuery, (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    
    let newNumber = 1;
    if (results.length > 0) {
      const lastId = results[0].nota_jual;
      const lastNumber = parseInt(lastId.slice(-3));
      newNumber = lastNumber + 1;
    }
    
    const newTransactionId = `${datePrefix}${String(newNumber).padStart(3, '0')}`;
    
    // Calculate total price
    let harga_total = 0;
    
    // Start transaction
    db.beginTransaction(err => {
      if (err) {
        return res.status(500).json({ error: err.message });
      }
      
      // Get product prices and calculate total
      const productIds = items.map(item => item.id_produk);
      const placeholders = productIds.map(() => '?').join(',');
      
      const getProductsQuery = `
        SELECT id_produk, harga, stok
        FROM produk
        WHERE id_produk IN (${placeholders})
      `;
      
      db.query(getProductsQuery, productIds, (err, products) => {
        if (err) {
          return db.rollback(() => {
            res.status(500).json({ error: err.message });
          });
        }
        
        // Create a map of product data for easy access
        const productsMap = {};
        products.forEach(product => {
          productsMap[product.id_produk] = product;
        });
        
        // Check stock and calculate total price
        for (const item of items) {
          const product = productsMap[item.id_produk];
          
          if (!product) {
            return db.rollback(() => {
              res.status(400).json({ message: `Product with ID ${item.id_produk} not found` });
            });
          }
          
          if (product.stok < item.quantity) {
            return db.rollback(() => {
              res.status(400).json({ message: `Insufficient stock for product ${item.id_produk}` });
            });
          }
          
          harga_total += product.harga * item.quantity;
        }
        
        // Get promo details if applicable
        let getPromoQuery = '';
        let subtotal_jual = harga_total;
        
        if (kode_promo) {
          getPromoQuery = 'SELECT * FROM promo WHERE kode_promo = ?';
          
          db.query(getPromoQuery, [kode_promo], (err, promoResults) => {
            if (err) {
              return db.rollback(() => {
                res.status(500).json({ error: err.message });
              });
            }
            
            // Apply discount if promo is valid
            if (promoResults.length > 0) {
              const promo = promoResults[0];
              
              // Check minimum purchase requirement
              if (harga_total >= promo.min_pembelian) {
                // Calculate discount
                let discount = (harga_total * promo.besar_potongan) / 100;
                
                // Apply maximum discount if applicable
                if (promo.maks_potongan && discount > promo.maks_potongan) {
                  discount = promo.maks_potongan;
                }
                
                subtotal_jual = harga_total - discount;
              } else {
                // Promo not applicable due to minimum purchase requirement
                return db.rollback(() => {
                  res.status(400).json({ 
                    message: `Promo code requires minimum purchase of ${promo.min_pembelian}` 
                  });
                });
              }
            } else {
              // Invalid promo code
              return db.rollback(() => {
                res.status(400).json({ message: 'Invalid promo code' });
              });
            }
            
            insertTransaction(subtotal_jual);
          });
        } else {
          insertTransaction(subtotal_jual);
        }
        
        // Function to insert transaction after calculations
        function insertTransaction(finalSubtotal) {
          // Insert transaction header
          const headerQuery = `
            INSERT INTO h_jual (nota_jual, id_customer, id_karyawan, harga_total, kode_promo, subtotal_jual)
            VALUES (?, ?, ?, ?, ?, ?)
          `;
          
          db.query(headerQuery, [
            newTransactionId, 
            id_customer, 
            id_karyawan, 
            harga_total, 
            kode_promo, 
            finalSubtotal
          ], (err, headerResult) => {
            if (err) {
              return db.rollback(() => {
                res.status(500).json({ error: err.message });
              });
            }
            
            // Insert transaction details and update stock
            let detailInsertPromises = [];
            let stockUpdatePromises = [];
            
            for (const item of items) {
              // Insert transaction detail
              const detailQuery = `
                INSERT INTO d_jual (nota_jual, id_produk, quantity)
                VALUES (?, ?, ?)
              `;
              
              const detailPromise = new Promise((resolve, reject) => {
                db.query(detailQuery, [
                  newTransactionId,
                  item.id_produk,
                  item.quantity
                ], (err, result) => {
                  if (err) reject(err);
                  else resolve(result);
                });
              });
              
              detailInsertPromises.push(detailPromise);
              
              // Update product stock
              const updateStockQuery = `
                UPDATE produk
                SET stok = stok - ?
                WHERE id_produk = ?
              `;
              
              const stockPromise = new Promise((resolve, reject) => {
                db.query(updateStockQuery, [
                  item.quantity,
                  item.id_produk
                ], (err, result) => {
                  if (err) reject(err);
                  else resolve(result);
                });
              });
              
              stockUpdatePromises.push(stockPromise);
            }
            
            // Execute all promises
            Promise.all([...detailInsertPromises, ...stockUpdatePromises])
              .then(() => {
                // Commit transaction
                db.commit(err => {
                  if (err) {
                    return db.rollback(() => {
                      res.status(500).json({ error: err.message });
                    });
                  }
                  
                  res.status(201).json({
                    message: 'Sales transaction created successfully',
                    nota_jual: newTransactionId,
                    subtotal_jual: finalSubtotal
                  });
                });
              })
              .catch(err => {
                db.rollback(() => {
                  res.status(500).json({ error: err.message });
                });
              });
          });
        }
      });
    });
  });
});

// Add product rating
router.post('/rating', (req, res) => {
  const { nota_jual, id_produk, rating } = req.body;
  
  // Validate required fields
  if (!nota_jual || !id_produk || !rating || rating < 1 || rating > 5) {
    return res.status(400).json({ 
      message: 'Transaction ID, Product ID, and Rating (1-5) are required' 
    });
  }
  
  // Check if the transaction and product exist
  const checkQuery = `
    SELECT * FROM d_jual
    WHERE nota_jual = ? AND id_produk = ?
  `;
  
  db.query(checkQuery, [nota_jual, id_produk], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    
    if (results.length === 0) {
      return res.status(404).json({ message: 'Transaction or product not found' });
    }
    
    // Insert or update rating
    const upsertQuery = `
      INSERT INTO rating_produk (nota_jual, id_produk, rating)
      VALUES (?, ?, ?)
      ON DUPLICATE KEY UPDATE rating = ?
    `;
    
    db.query(upsertQuery, [nota_jual, id_produk, rating, rating], (err, result) => {
      if (err) {
        return res.status(500).json({ error: err.message });
      }
      
      res.json({
        message: 'Rating added successfully',
        nota_jual,
        id_produk,
        rating
      });
    });
  });
});

// Get all purchase transactions
router.get('/purchase', (req, res) => {
  const query = `
    SELECT h.*, s.nama_supplier, k.nama_karyawan
    FROM h_beli h
    LEFT JOIN supplier s ON h.id_supplier = s.id_supplier
    LEFT JOIN karyawan k ON h.id_karyawan = k.id_karyawan
    ORDER BY h.nota_beli DESC
  `;
  
  db.query(query, (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    res.json(results);
  });
});

// Get a specific purchase transaction
router.get('/purchase/:id', (req, res) => {
  const purchaseId = req.params.id;
  
  const headerQuery = `
    SELECT h.*, s.nama_supplier, k.nama_karyawan
    FROM h_beli h
    LEFT JOIN supplier s ON h.id_supplier = s.id_supplier
    LEFT JOIN karyawan k ON h.id_karyawan = k.id_karyawan
    WHERE h.nota_beli = ?
  `;
  
  const detailQuery = `
    SELECT d.*, p.nama_produk
    FROM d_beli d
    INNER JOIN produk p ON d.id_produk = p.id_produk
    WHERE d.nota_beli = ?
  `;
  
  db.query(headerQuery, [purchaseId], (err, headerResults) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    
    if (headerResults.length === 0) {
      return res.status(404).json({ message: 'Purchase transaction not found' });
    }
    
    db.query(detailQuery, [purchaseId], (err, detailResults) => {
      if (err) {
        return res.status(500).json({ error: err.message });
      }
      
      res.json({
        header: headerResults[0],
        details: detailResults
      });
    });
  });
});

// Create a new purchase transaction
router.post('/purchase', (req, res) => {
  const { id_supplier, id_karyawan, items } = req.body;
  
  // Validate required fields
  if (!id_supplier || !id_karyawan || !items || !items.length) {
    return res.status(400).json({ message: 'Supplier ID, Employee ID, and items are required' });
  }
  
  // Generate new transaction ID
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  
  // Get the last transaction number for today
  const datePrefix = `PB${year}${month}${day}`;
  const getLastIdQuery = `
    SELECT nota_beli
    FROM h_beli
    WHERE nota_beli LIKE '${datePrefix}%'
    ORDER BY nota_beli DESC
    LIMIT 1
  `;
  
  db.query(getLastIdQuery, (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    
    let newNumber = 1;
    if (results.length > 0) {
      const lastId = results[0].nota_beli;
      const lastNumber = parseInt(lastId.slice(-3));
      newNumber = lastNumber + 1;
    }
    
    const newTransactionId = `${datePrefix}${String(newNumber).padStart(3, '0')}`;
    
    // Calculate subtotal
    let subtotal_beli = 0;
    for (const item of items) {
      subtotal_beli += item.harga_beli * item.quantity;
    }
    
    // Start transaction
    db.beginTransaction(err => {
      if (err) {
        return res.status(500).json({ error: err.message });
      }
      
      // Insert transaction header
      const headerQuery = `
        INSERT INTO h_beli (nota_beli, id_karyawan, id_supplier, subtotal_beli)
        VALUES (?, ?, ?, ?)
      `;
      
      db.query(headerQuery, [
        newTransactionId, 
        id_karyawan, 
        id_supplier, 
        subtotal_beli
      ], (err, headerResult) => {
        if (err) {
          return db.rollback(() => {
            res.status(500).json({ error: err.message });
          });
        }
        
        // Insert transaction details and update stock
        let detailInsertPromises = [];
        let stockUpdatePromises = [];
        
        for (const item of items) {
          // Check if product exists
          const checkProductQuery = `
            SELECT id_produk FROM produk WHERE id_produk = ?
          `;
          
          db.query(checkProductQuery, [item.id_produk], (err, productResult) => {
            if (err) {
              return db.rollback(() => {
                res.status(500).json({ error: err.message });
              });
            }
            
            if (productResult.length === 0) {
              return db.rollback(() => {
                res.status(400).json({ message: `Product with ID ${item.id_produk} not found` });
              });
            }
            
            // Insert transaction detail
            const detailQuery = `
              INSERT INTO d_beli (nota_beli, id_produk, harga_beli, quantity)
              VALUES (?, ?, ?, ?)
            `;
            
            const detailPromise = new Promise((resolve, reject) => {
              db.query(detailQuery, [
                newTransactionId,
                item.id_produk,
                item.harga_beli,
                item.quantity
              ], (err, result) => {
                if (err) reject(err);
                else resolve(result);
              });
            });
            
            detailInsertPromises.push(detailPromise);
            
            // Update product stock
            const updateStockQuery = `
              UPDATE produk
              SET stok = stok + ?
              WHERE id_produk = ?
            `;
            
            const stockPromise = new Promise((resolve, reject) => {
              db.query(updateStockQuery, [
                item.quantity,
                item.id_produk
              ], (err, result) => {
                if (err) reject(err);
                else resolve(result);
              });
            });
            
            stockUpdatePromises.push(stockPromise);
          });
        }
        
        // Wait for all database operations to complete
        setTimeout(() => {
          Promise.all([...detailInsertPromises, ...stockUpdatePromises])
            .then(() => {
              // Commit transaction
              db.commit(err => {
                if (err) {
                  return db.rollback(() => {
                    res.status(500).json({ error: err.message });
                  });
                }
                
                res.status(201).json({
                  message: 'Purchase transaction created successfully',
                  nota_beli: newTransactionId,
                  subtotal_beli
                });
              });
            })
            .catch(err => {
              db.rollback(() => {
                res.status(500).json({ error: err.message });
              });
            });
        }, 100);
      });
    });
  });
});

// Get all promo codes
router.get('/promos', (req, res) => {
  const query = 'SELECT * FROM promo';
  
  db.query(query, (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    res.json(results);
  });
});

// Get a specific promo code
router.get('/promos/:id', (req, res) => {
  const promoId = req.params.id;
  const query = 'SELECT * FROM promo WHERE kode_promo = ?';
  
  db.query(query, [promoId], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    
    if (results.length === 0) {
      return res.status(404).json({ message: 'Promo code not found' });
    }
    
    res.json(results[0]);
  });
});

// Create a new promo code
router.post('/promos', (req, res) => {
  const { kode_promo, besar_potongan, maks_potongan, min_pembelian } = req.body;
  
  // Validate required fields
  if (!kode_promo || !besar_potongan || !min_pembelian) {
    return res.status(400).json({ 
      message: 'Promo code, discount percentage, and minimum purchase are required' 
    });
  }
  
  const query = `
    INSERT INTO promo (kode_promo, besar_potongan, maks_potongan, min_pembelian)
    VALUES (?, ?, ?, ?)
  `;
  
  db.query(query, [kode_promo, besar_potongan, maks_potongan, min_pembelian], (err, result) => {
    if (err) {
      // Check for duplicate entry
      if (err.code === 'ER_DUP_ENTRY') {
        return res.status(400).json({ message: 'Promo code already exists' });
      }
      return res.status(500).json({ error: err.message });
    }
    
    res.status(201).json({
      message: 'Promo code created successfully',
      kode_promo
    });
  });
});

// Update a promo code
router.put('/promos/:id', (req, res) => {
  const promoId = req.params.id;
  const { besar_potongan, maks_potongan, min_pembelian } = req.body;
  
  // Validate required fields
  if (!besar_potongan || !min_pembelian) {
    return res.status(400).json({ 
      message: 'Discount percentage and minimum purchase are required' 
    });
  }
  
  const query = `
    UPDATE promo
    SET besar_potongan = ?, maks_potongan = ?, min_pembelian = ?
    WHERE kode_promo = ?
  `;
  
  db.query(query, [besar_potongan, maks_potongan, min_pembelian, promoId], (err, result) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ message: 'Promo code not found' });
    }
    
    res.json({
      message: 'Promo code updated successfully',
      kode_promo: promoId
    });
  });
});

// Delete a promo code
router.delete('/promos/:id', (req, res) => {
  const promoId = req.params.id;
  
  // Check if the promo is being used
  const checkQuery = 'SELECT COUNT(*) AS count FROM h_jual WHERE kode_promo = ?';
  
  db.query(checkQuery, [promoId], (err, results) => {
    if (err) {
      return res.status(500).json({ error: err.message });
    }
    
    if (results[0].count > 0) {
      return res.status(400).json({ 
        message: 'Cannot delete promo code as it is used in transactions' 
      });
    }
    
    const deleteQuery = 'DELETE FROM promo WHERE kode_promo = ?';
    
    db.query(deleteQuery, [promoId], (err, result) => {
      if (err) {
        return res.status(500).json({ error: err.message });
      }
      
      if (result.affectedRows === 0) {
        return res.status(404).json({ message: 'Promo code not found' });
      }
      
      res.json({
        message: 'Promo code deleted successfully',
        kode_promo: promoId
      });
    });
  });
});

module.exports = router;