// Category routes for the Rubik's Cube Shop API
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

// GET all categories
router.get('/', (req, res) => {
  const query = 'SELECT * FROM kategori';
  
  db.query(query, (err, results) => {
    if (err) {
      console.error('Error fetching categories:', err);
      return res.status(500).json({ error: 'Error fetching categories' });
    }
    res.json(results);
  });
});

// GET category by ID
router.get('/:id', (req, res) => {
  const categoryId = req.params.id;
  const query = 'SELECT * FROM kategori WHERE id_kategori = ?';
  
  db.query(query, [categoryId], (err, results) => {
    if (err) {
      console.error('Error fetching category:', err);
      return res.status(500).json({ error: 'Error fetching category' });
    }
    
    if (results.length === 0) {
      return res.status(404).json({ error: 'Category not found' });
    }
    
    res.json(results[0]);
  });
});

// Create new category
router.post('/', (req, res) => {
  const { id_kategori, nama_kategori } = req.body;
  
  // Validate required fields
  if (!id_kategori || !nama_kategori) {
    return res.status(400).json({ error: 'Category ID and name are required' });
  }
  
  const query = 'INSERT INTO kategori (id_kategori, nama_kategori) VALUES (?, ?)';
  
  db.query(query, [id_kategori, nama_kategori], (err, results) => {
    if (err) {
      console.error('Error creating category:', err);
      return res.status(500).json({ error: 'Error creating category' });
    }
    
    res.status(201).json({
      message: 'Category created successfully',
      id: id_kategori
    });
  });
});

// Update category
router.put('/:id', (req, res) => {
  const categoryId = req.params.id;
  const { nama_kategori } = req.body;
  
  // Validate required fields
  if (!nama_kategori) {
    return res.status(400).json({ error: 'Category name is required' });
  }
  
  // Check if category exists
  db.query('SELECT * FROM kategori WHERE id_kategori = ?', [categoryId], (err, results) => {
    if (err) {
      console.error('Error checking category:', err);
      return res.status(500).json({ error: 'Error checking category' });
    }
    
    if (results.length === 0) {
      return res.status(404).json({ error: 'Category not found' });
    }
    
    // Update category
    const query = 'UPDATE kategori SET nama_kategori = ? WHERE id_kategori = ?';
    
    db.query(query, [nama_kategori, categoryId], (err, results) => {
      if (err) {
        console.error('Error updating category:', err);
        return res.status(500).json({ error: 'Error updating category' });
      }
      
      res.json({
        message: 'Category updated successfully',
        id: categoryId
      });
    });
  });
});

// Delete category
router.delete('/:id', (req, res) => {
  const categoryId = req.params.id;
  
  // Check if category exists
  db.query('SELECT * FROM kategori WHERE id_kategori = ?', [categoryId], (err, results) => {
    if (err) {
      console.error('Error checking category:', err);
      return res.status(500).json({ error: 'Error checking category' });
    }
    
    if (results.length === 0) {
      return res.status(404).json({ error: 'Category not found' });
    }
    
    // Check if category is used in products
    db.query('SELECT COUNT(*) as count FROM produk WHERE id_kategori = ?', [categoryId], (err, results) => {
      if (err) {
        console.error('Error checking category usage:', err);
        return res.status(500).json({ error: 'Error checking category usage' });
      }
      
      if (results[0].count > 0) {
        return res.status(400).json({ 
          error: 'Cannot delete category. It is being used by products.',
          productsCount: results[0].count
        });
      }
      
      // Delete category
      const query = 'DELETE FROM kategori WHERE id_kategori = ?';
      
      db.query(query, [categoryId], (err, results) => {
        if (err) {
          console.error('Error deleting category:', err);
          return res.status(500).json({ error: 'Error deleting category' });
        }
        
        res.json({
          message: 'Category deleted successfully',
          id: categoryId
        });
      });
    });
  });
});

// Get products by category
router.get('/:id/products', (req, res) => {
  const categoryId = req.params.id;
  const query = `
    SELECT p.*, k.nama_kategori, j.nama_jenis, m.nama_merk
    FROM produk p
    LEFT JOIN kategori k ON p.id_kategori = k.id_kategori
    LEFT JOIN jenis j ON p.id_jenis = j.id_jenis
    LEFT JOIN merk m ON p.id_merk = m.id_merk
    WHERE p.id_kategori = ?
  `;
  
  db.query(query, [categoryId], (err, results) => {
    if (err) {
      console.error('Error fetching products by category:', err);
      return res.status(500).json({ error: 'Error fetching products by category' });
    }
    
    res.json(results);
  });
});

// Get all types (jenis)
router.get('/types/all', (req, res) => {
  const query = 'SELECT * FROM jenis';
  
  db.query(query, (err, results) => {
    if (err) {
      console.error('Error fetching types:', err);
      return res.status(500).json({ error: 'Error fetching types' });
    }
    res.json(results);
  });
});

// Get all brands (merk)
router.get('/brands/all', (req, res) => {
  const query = 'SELECT * FROM merk';
  
  db.query(query, (err, results) => {
    if (err) {
      console.error('Error fetching brands:', err);
      return res.status(500).json({ error: 'Error fetching brands' });
    }
    res.json(results);
  });
});

module.exports = router;