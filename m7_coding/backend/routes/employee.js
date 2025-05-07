const express = require('express');
const router = express.Router();
const mysql = require('mysql');

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'db_rub'
});

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