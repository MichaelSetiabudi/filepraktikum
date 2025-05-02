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

// Get sales report (daily, weekly, monthly)
router.get('/sales/:period', (req, res) => {
    const { period } = req.params;
    let query = '';
    
    if (period === 'daily') {
        query = `
            SELECT 
                DATE(t.transaction_date) AS date,
                COUNT(t.transaction_id) AS total_transactions,
                SUM(td.quantity * p.price) AS total_revenue
            FROM 
                transactions t
            JOIN 
                transaction_details td ON t.transaction_id = td.transaction_id
            JOIN 
                products p ON td.product_id = p.product_id
            WHERE 
                t.transaction_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            GROUP BY 
                DATE(t.transaction_date)
            ORDER BY 
                date DESC
        `;
    } else if (period === 'weekly') {
        query = `
            SELECT 
                YEARWEEK(t.transaction_date, 1) AS year_week,
                MIN(DATE(t.transaction_date)) AS week_start,
                COUNT(t.transaction_id) AS total_transactions,
                SUM(td.quantity * p.price) AS total_revenue
            FROM 
                transactions t
            JOIN 
                transaction_details td ON t.transaction_id = td.transaction_id
            JOIN 
                products p ON td.product_id = p.product_id
            WHERE 
                t.transaction_date >= DATE_SUB(CURDATE(), INTERVAL 12 WEEK)
            GROUP BY 
                YEARWEEK(t.transaction_date, 1)
            ORDER BY 
                year_week DESC
        `;
    } else if (period === 'monthly') {
        query = `
            SELECT 
                DATE_FORMAT(t.transaction_date, '%Y-%m') AS month,
                COUNT(t.transaction_id) AS total_transactions,
                SUM(td.quantity * p.price) AS total_revenue
            FROM 
                transactions t
            JOIN 
                transaction_details td ON t.transaction_id = td.transaction_id
            JOIN 
                products p ON td.product_id = p.product_id
            WHERE 
                t.transaction_date >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
            GROUP BY 
                DATE_FORMAT(t.transaction_date, '%Y-%m')
            ORDER BY 
                month DESC
        `;
    } else {
        return res.status(400).json({ message: 'Invalid period. Use daily, weekly, or monthly.' });
    }

    db.query(query, (err, results) => {
        if (err) {
            console.error('Error fetching sales report:', err);
            return res.status(500).json({ message: 'Error fetching sales report', error: err });
        }
        
        res.json({
            period: period,
            data: results
        });
    });
});

// Get inventory report
router.get('/inventory', (req, res) => {
    const query = `
        SELECT 
            p.product_id,
            p.name,
            p.description,
            p.price,
            p.stock,
            c.name AS category_name,
            s.name AS supplier_name
        FROM 
            products p
        LEFT JOIN 
            categories c ON p.category_id = c.category_id
        LEFT JOIN 
            suppliers s ON p.supplier_id = s.supplier_id
        ORDER BY 
            p.stock ASC
    `;

    db.query(query, (err, results) => {
        if (err) {
            console.error('Error fetching inventory report:', err);
            return res.status(500).json({ message: 'Error fetching inventory report', error: err });
        }
        
        res.json({
            total_products: results.length,
            data: results
        });
    });
});

// Get top selling products
router.get('/top-products', (req, res) => {
    const limit = req.query.limit || 10;
    
    const query = `
        SELECT 
            p.product_id,
            p.name,
            p.description,
            p.price,
            SUM(td.quantity) AS total_sold,
            SUM(td.quantity * p.price) AS total_revenue
        FROM 
            products p
        JOIN 
            transaction_details td ON p.product_id = td.product_id
        JOIN 
            transactions t ON td.transaction_id = t.transaction_id
        WHERE 
            t.transaction_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
        GROUP BY 
            p.product_id
        ORDER BY 
            total_sold DESC
        LIMIT ?
    `;

    db.query(query, [parseInt(limit)], (err, results) => {
        if (err) {
            console.error('Error fetching top products:', err);
            return res.status(500).json({ message: 'Error fetching top products', error: err });
        }
        
        res.json({
            timeframe: 'Last 3 months',
            data: results
        });
    });
});

// Get customer purchase report
router.get('/customer-purchases', (req, res) => {
    const limit = req.query.limit || 10;
    
    const query = `
        SELECT 
            c.customer_id,
            c.name,
            c.email,
            COUNT(t.transaction_id) AS total_transactions,
            SUM(td.quantity * p.price) AS total_spent
        FROM 
            customers c
        JOIN 
            transactions t ON c.customer_id = t.customer_id
        JOIN 
            transaction_details td ON t.transaction_id = td.transaction_id
        JOIN 
            products p ON td.product_id = p.product_id
        GROUP BY 
            c.customer_id
        ORDER BY 
            total_spent DESC
        LIMIT ?
    `;

    db.query(query, [parseInt(limit)], (err, results) => {
        if (err) {
            console.error('Error fetching customer purchases:', err);
            return res.status(500).json({ message: 'Error fetching customer purchases', error: err });
        }
        
        res.json({
            data: results
        });
    });
});

// Get revenue by category
router.get('/category-revenue', (req, res) => {
    const query = `
        SELECT 
            c.category_id,
            c.name AS category_name,
            COUNT(td.detail_id) AS total_items_sold,
            SUM(td.quantity) AS total_quantity,
            SUM(td.quantity * p.price) AS total_revenue
        FROM 
            categories c
        JOIN 
            products p ON c.category_id = p.category_id
        JOIN 
            transaction_details td ON p.product_id = td.product_id
        JOIN 
            transactions t ON td.transaction_id = t.transaction_id
        WHERE 
            t.transaction_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
        GROUP BY 
            c.category_id
        ORDER BY 
            total_revenue DESC
    `;

    db.query(query, (err, results) => {
        if (err) {
            console.error('Error fetching category revenue:', err);
            return res.status(500).json({ message: 'Error fetching category revenue', error: err });
        }
        
        res.json({
            timeframe: 'Last 6 months',
            data: results
        });
    });
});

module.exports = router;