// Customer routes for the Rubik's Cube Shop API

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

// Get all customers
router.get('/', (req, res) => {
  const query = 'SELECT * FROM customer';
  
  db.query(query, (err, results) => {
    if (err) {
      console.error('Error fetching customers:', err);
      return res.status(500).json({ error: 'Database error' });
    }
    res.json(results);
  });
});

// Get customer by ID
router.get('/:id', (req, res) => {
  const customerId = req.params.id;
  const query = 'SELECT * FROM customer WHERE id_customer = ?';
  
  db.query(query, [customerId], (err, results) => {
    if (err) {
      console.error('Error fetching customer:', err);
      return res.status(500).json({ error: 'Database error' });
    }
    
    if (results.length === 0) {
      return res.status(404).json({ error: 'Customer not found' });
    }
    
    res.json(results[0]);
  });
});

// Create new customer
router.post('/', (req, res) => {
  const { id_customer, nama_customer, jk_customer, alamat_customer, noTelp_customer } = req.body;
  
  // Validate request
  if (!id_customer || !nama_customer || !jk_customer || !alamat_customer || !noTelp_customer) {
    return res.status(400).json({ error: 'All fields are required' });
  }
  
  const query = 'INSERT INTO customer (id_customer, nama_customer, jk_customer, alamat_customer, noTelp_customer) VALUES (?, ?, ?, ?, ?)';
  
  db.query(query, [id_customer, nama_customer, jk_customer, alamat_customer, noTelp_customer], (err, result) => {
    if (err) {
      console.error('Error creating customer:', err);
      return res.status(500).json({ error: 'Database error' });
    }
    
    res.status(201).json({ 
      message: 'Customer created successfully',
      id: id_customer
    });
  });
});

// Update customer
router.put('/:id', (req, res) => {
  const customerId = req.params.id;
  const { nama_customer, jk_customer, alamat_customer, noTelp_customer } = req.body;
  
  // Validate request
  if (!nama_customer || !jk_customer || !alamat_customer || !noTelp_customer) {
    return res.status(400).json({ error: 'All fields are required' });
  }
  
  const query = 'UPDATE customer SET nama_customer = ?, jk_customer = ?, alamat_customer = ?, noTelp_customer = ? WHERE id_customer = ?';
  
  db.query(query, [nama_customer, jk_customer, alamat_customer, noTelp_customer, customerId], (err, result) => {
    if (err) {
      console.error('Error updating customer:', err);
      return res.status(500).json({ error: 'Database error' });
    }
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: 'Customer not found' });
    }
    
    res.json({ message: 'Customer updated successfully' });
  });
});

// Delete customer
router.delete('/:id', (req, res) => {
  const customerId = req.params.id;
  const query = 'DELETE FROM customer WHERE id_customer = ?';
  
  db.query(query, [customerId], (err, result) => {
    if (err) {
      console.error('Error deleting customer:', err);
      return res.status(500).json({ error: 'Database error' });
    }
    
    if (result.affectedRows === 0) {
      return res.status(404).json({ error: 'Customer not found' });
    }
    
    res.json({ message: 'Customer deleted successfully' });
  });
});

// Get customer purchase history
router.get('/:id/transactions', (req, res) => {
  const customerId = req.params.id;
  const query = `
    SELECT h.nota_jual, h.harga_total, h.subtotal_jual, h.kode_promo, 
           p.id_produk, p.nama_produk, d.quantity
    FROM h_jual h
    JOIN d_jual d ON h.nota_jual = d.nota_jual
    JOIN produk p ON d.id_produk = p.id_produk
    WHERE h.id_customer = ?
    ORDER BY h.nota_jual DESC
  `;
  
  db.query(query, [customerId], (err, results) => {
    if (err) {
      console.error('Error fetching customer transactions:', err);
      return res.status(500).json({ error: 'Database error' });
    }
    
    // Group by transaction
    const transactions = {};
    
    results.forEach(row => {
      if (!transactions[row.nota_jual]) {
        transactions[row.nota_jual] = {
          nota_jual: row.nota_jual,
          harga_total: row.harga_total,
          subtotal_jual: row.subtotal_jual,
          kode_promo: row.kode_promo,
          items: []
        };
      }
      
      transactions[row.nota_jual].items.push({
        id_produk: row.id_produk,
        nama_produk: row.nama_produk,
        quantity: row.quantity
      });
    });
    
    res.json(Object.values(transactions));
  });
});

module.exports = router;