  // Employee routes for the Rubik's Cube Shop API
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

  // GET all employees
  router.get('/', (req, res) => {
    const query = 'SELECT * FROM karyawan';
    
    db.query(query, (err, results) => {
      if (err) {
        console.error('Error fetching employees:', err);
        return res.status(500).json({ error: 'Error fetching employees' });
      }
      res.json(results);
    });
  });

  // GET employee by ID
  router.get('/:id', (req, res) => {
    const employeeId = req.params.id;
    const query = 'SELECT * FROM karyawan WHERE id_karyawan = ?';
    
    db.query(query, [employeeId], (err, results) => {
      if (err) {
        console.error('Error fetching employee:', err);
        return res.status(500).json({ error: 'Error fetching employee' });
      }
      
      if (results.length === 0) {
        return res.status(404).json({ error: 'Employee not found' });
      }
      
      res.json(results[0]);
    });
  });

  // Create new employee
  router.post('/', (req, res) => {
    const { id_karyawan, nama_karyawan, jk_karyawan, alamat_karyawan, noTelp_karyawan, dob_karyawan, tgl_masuk, status_karyawan } = req.body;
    
    // Validate required fields
    if (!id_karyawan || !nama_karyawan) {
      return res.status(400).json({ error: 'Employee ID and name are required' });
    }
    
    const query = 'INSERT INTO karyawan (id_karyawan, nama_karyawan, jk_karyawan, alamat_karyawan, noTelp_karyawan, dob_karyawan, tgl_masuk, status_karyawan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)';
    
    db.query(query, [id_karyawan, nama_karyawan, jk_karyawan, alamat_karyawan, noTelp_karyawan, dob_karyawan, tgl_masuk, status_karyawan], (err, results) => {
      if (err) {
        console.error('Error creating employee:', err);
        return res.status(500).json({ error: 'Error creating employee' });
      }
      
      res.status(201).json({
        message: 'Employee created successfully',
        id: id_karyawan
      });
    });
  });

  // Update employee
  router.put('/:id', (req, res) => {
    const employeeId = req.params.id;
    const { nama_karyawan, jk_karyawan, alamat_karyawan, noTelp_karyawan, dob_karyawan, tgl_masuk, status_karyawan } = req.body;
    
    // Check if employee exists
    db.query('SELECT * FROM karyawan WHERE id_karyawan = ?', [employeeId], (err, results) => {
      if (err) {
        console.error('Error checking employee:', err);
        return res.status(500).json({ error: 'Error checking employee' });
      }
      
      if (results.length === 0) {
        return res.status(404).json({ error: 'Employee not found' });
      }
      
      // Update employee
      const query = 'UPDATE karyawan SET nama_karyawan = ?, jk_karyawan = ?, alamat_karyawan = ?, noTelp_karyawan = ?, dob_karyawan = ?, tgl_masuk = ?, status_karyawan = ? WHERE id_karyawan = ?';
      
      db.query(query, [nama_karyawan, jk_karyawan, alamat_karyawan, noTelp_karyawan, dob_karyawan, tgl_masuk, status_karyawan, employeeId], (err, results) => {
        if (err) {
          console.error('Error updating employee:', err);
          return res.status(500).json({ error: 'Error updating employee' });
        }
        
        res.json({
          message: 'Employee updated successfully',
          id: employeeId
        });
      });
    });
  });

  // Delete employee
  router.delete('/:id', (req, res) => {
    const employeeId = req.params.id;
    
    // Check if employee exists
    db.query('SELECT * FROM karyawan WHERE id_karyawan = ?', [employeeId], (err, results) => {
      if (err) {
        console.error('Error checking employee:', err);
        return res.status(500).json({ error: 'Error checking employee' });
      }
      
      if (results.length === 0) {
        return res.status(404).json({ error: 'Employee not found' });
      }
      
      // Delete employee
      const query = 'DELETE FROM karyawan WHERE id_karyawan = ?';
      
      db.query(query, [employeeId], (err, results) => {
        if (err) {
          console.error('Error deleting employee:', err);
          return res.status(500).json({ error: 'Error deleting employee' });
        }
        
        res.json({
          message: 'Employee deleted successfully',
          id: employeeId
        });
      });
    });
  });

  // Get active employees
  router.get('/status/active', (req, res) => {
    const query = 'SELECT * FROM karyawan WHERE status_karyawan = "1"';
    
    db.query(query, (err, results) => {
      if (err) {
        console.error('Error fetching active employees:', err);
        return res.status(500).json({ error: 'Error fetching active employees' });
      }
      
      res.json(results);
    });
  });

  module.exports = router;