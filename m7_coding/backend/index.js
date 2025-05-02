    // Main entry point for the Rubik's Cube Shop API

    const express = require('express');
    const mysql = require('mysql');
    const bodyParser = require('body-parser');

    const app = express();
    const port = 3000;

    // Middleware
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: true }));

    // MySQL Connection
    const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'db_rub'
    });

    // Connect to MySQL
    db.connect((err) => {
    if (err) {
        console.error('Error connecting to MySQL database:', err);
        return;
    }
    console.log('Connected to MySQL database');
    });

    // Import routes
    const customerRoutes = require('./routes/customer');
    const productRoutes = require('./routes/product');
    const transactionRoutes = require('./routes/transaction');
    const employeeRoutes = require('./routes/employee');
    const supplierRoutes = require('./routes/supplier');
    const categoryRoutes = require('./routes/category');
    const reportRoutes = require('./routes/report');

    // Use routes
    app.use('/api/customer', customerRoutes);
    app.use('/api/product', productRoutes);
    app.use('/api/transaction', transactionRoutes);
    app.use('/api/employee', employeeRoutes);
    app.use('/api/supplier', supplierRoutes);
    app.use('/api/category', categoryRoutes);
    app.use('/api/report', reportRoutes);

    // Root route
    app.get('/', (req, res) => {
    res.json({ message: 'Welcome to Rubik\'s Cube Shop API' });
    });

    // Start the server
    app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
    });

    module.exports = app;