const express = require('express');
const router = express.Router();
const mysql = require('mysql');

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'db_rub'
});

router.get("/transaction-report/:nama_karyawan", (req, res) => {
    const { nama_karyawan } = req.params;
    db.query(
        `SELECT h.nota_jual as nota_jual, c.nama_customer as nama_customer, k.nama_karyawan as nama_karyawan, p.nama_produk as nama_produk, d.quantity as quantity, p.harga as harga, h.subtotal_jual as subtotal_jual
      FROM h_jual h
      join d_jual d on d.nota_jual = h.nota_jual
      join produk p on p.id_produk = d.id_produk
      join customer c on c.id_customer = h.id_customer
      join karyawan k on k.id_karyawan = h.id_karyawan
      where k.nama_karyawan = ?`, [nama_karyawan],
        (err, results) => {
            if (err) return res.status(500).json({ message: err.message });
            const history = {};
            results.map((item) => {
                const detail = `${item.nama_produk} - ${item.quantity} × Rp${item.harga} = Rp${item.subtotal_jual}`;
                if (!history[item.nota_jual]) {
                    history[item.nota_jual] = {
                        transaction_id: item.nota_jual,
                        customer_name: item.nama_customer,
                        cashier_name: item.nama_karyawan,
                        total: item.subtotal_jual,
                        detail: [],
                    };
                }
                history[item.nota_jual].detail.push(detail);
            });
            res.status(200).json(Object.values(history));
        }
    );
});
router.get("/transaction-history/:id_karyawan", (req, res) => {
    const { id_karyawan } = req.params;
    db.query(
        `SELECT h.nota_jual as nota_jual, c.nama_customer as nama_customer, k.nama_karyawan as nama_karyawan, p.nama_produk as nama_produk, d.quantity as quantity, p.harga as harga, h.subtotal_jual as subtotal_jual
    FROM h_jual h
    join d_jual d on d.nota_jual = h.nota_jual
    join produk p on p.id_produk = d.id_produk
    join customer c on c.id_customer = h.id_customer
    join karyawan k on k.id_karyawan = h.id_karyawan
    where h.id_karyawan = ?`, [id_karyawan],
        (err, results) => {
            if (err) return res.status(500).json({ message: err.message });
            const history = {};
            results.map((item) => {
                const detail = `${item.nama_produk} - ${item.quantity} × Rp${item.harga} = Rp${item.subtotal_jual}`;
                if (!history[item.nota_jual]) {
                    history[item.nota_jual] = {
                        transaction_id: item.nota_jual,
                        customer_name: item.nama_customer,
                        cashier_name: item.nama_karyawan,
                        total: item.subtotal_jual,
                        detail: [],
                    };
                }
                history[item.nota_jual].detail.push(detail);
            });
            res.status(200).json(Object.values(history));
        }
    );
});

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
router.post('/sales', (req, res) => {
    const { id_customer, id_karyawan, kode_promo, items } = req.body;

    if (!id_customer || !id_karyawan || !items || !items.length) {
        return res.status(400).json({ message: 'Customer ID, Employee ID, and items are required' });
    }

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');

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

        let harga_total = 0;

        db.beginTransaction(err => {
            if (err) {
                return res.status(500).json({ error: err.message });
            }

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

                const productsMap = {};
                products.forEach(product => {
                    productsMap[product.id_produk] = product;
                });

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

                        if (promoResults.length > 0) {
                            const promo = promoResults[0];

                            if (harga_total >= promo.min_pembelian) {
                                let discount = (harga_total * promo.besar_potongan) / 100;

                                if (promo.maks_potongan && discount > promo.maks_potongan) {
                                    discount = promo.maks_potongan;
                                }

                                subtotal_jual = harga_total - discount;
                            } else {
                                return db.rollback(() => {
                                    res.status(400).json({
                                        message: `Promo code requires minimum purchase of ${promo.min_pembelian}`
                                    });
                                });
                            }
                        } else {
                            return db.rollback(() => {
                                res.status(400).json({ message: 'Invalid promo code' });
                            });
                        }

                        insertTransaction(subtotal_jual);
                    });
                } else {
                    insertTransaction(subtotal_jual);
                }

                function insertTransaction(finalSubtotal) {
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

                        let detailInsertPromises = [];
                        let stockUpdatePromises = [];

                        for (const item of items) {
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

                        Promise.all([...detailInsertPromises, ...stockUpdatePromises])
                            .then(() => {
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

router.post('/purchase', (req, res) => {
    const { id_supplier, id_karyawan, items } = req.body;

    if (!id_supplier || !id_karyawan || !items || !items.length) {
        return res.status(400).json({ message: 'Supplier ID, Employee ID, and items are required' });
    }

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');

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

        let subtotal_beli = 0;
        for (const item of items) {
            subtotal_beli += item.harga_beli * item.quantity;
        }

        db.beginTransaction(err => {
            if (err) {
                return res.status(500).json({ error: err.message });
            }

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

                let detailInsertPromises = [];
                let stockUpdatePromises = [];

                for (const item of items) {
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

                setTimeout(() => {
                    Promise.all([...detailInsertPromises, ...stockUpdatePromises])
                        .then(() => {
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


module.exports = router;