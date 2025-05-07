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
    const query = 'SELECT * FROM supplier';

    db.query(query, (err, results) => {
        if (err) {
            console.error('Error fetching suppliers:', err);
            return res.status(500).json({ error: 'Error fetching suppliers' });
        }
        res.json(results);
    });
});

router.get('/:id', (req, res) => {
    const supplierId = req.params.id;
    const query = 'SELECT * FROM supplier WHERE id_supplier = ?';

    db.query(query, [supplierId], (err, results) => {
        if (err) {
            console.error('Error fetching supplier:', err);
            return res.status(500).json({ error: 'Error fetching supplier' });
        }

        if (results.length === 0) {
            return res.status(404).json({ error: 'Supplier not found' });
        }

        res.json(results[0]);
    });
});

router.post('/', (req, res) => {
    const { id_supplier, nama_supplier, cp_supplier, pn_supplier, alamat_supplier } = req.body;

    if (!id_supplier || !nama_supplier) {
        return res.status(400).json({ error: 'Supplier ID and name are required' });
    }

    const query = 'INSERT INTO supplier (id_supplier, nama_supplier, cp_supplier, pn_supplier, alamat_supplier) VALUES (?, ?, ?, ?, ?)';

    db.query(query, [id_supplier, nama_supplier, cp_supplier, pn_supplier, alamat_supplier], (err, results) => {
        if (err) {
            console.error('Error creating supplier:', err);
            return res.status(500).json({ error: 'Error creating supplier' });
        }

        res.status(201).json({
            message: 'Supplier created successfully',
            id: id_supplier
        });
    });
});

router.put('/:id', (req, res) => {
    const supplierId = req.params.id;
    const { nama_supplier, cp_supplier, pn_supplier, alamat_supplier } = req.body;

    db.query('SELECT * FROM supplier WHERE id_supplier = ?', [supplierId], (err, results) => {
        if (err) {
            console.error('Error checking supplier:', err);
            return res.status(500).json({ error: 'Error checking supplier' });
        }

        if (results.length === 0) {
            return res.status(404).json({ error: 'Supplier not found' });
        }

        const query = 'UPDATE supplier SET nama_supplier = ?, cp_supplier = ?, pn_supplier = ?, alamat_supplier = ? WHERE id_supplier = ?';

        db.query(query, [nama_supplier, cp_supplier, pn_supplier, alamat_supplier, supplierId], (err, results) => {
            if (err) {
                console.error('Error updating supplier:', err);
                return res.status(500).json({ error: 'Error updating supplier' });
            }

            res.json({
                message: 'Supplier updated successfully',
                id: supplierId
            });
        });
    });
});

router.delete('/:id', (req, res) => {
    const supplierId = req.params.id;

    db.query('SELECT * FROM supplier WHERE id_supplier = ?', [supplierId], (err, results) => {
        if (err) {
            console.error('Error checking supplier:', err);
            return res.status(500).json({ error: 'Error checking supplier' });
        }

        if (results.length === 0) {
            return res.status(404).json({ error: 'Supplier not found' });
        }

        const query = 'DELETE FROM supplier WHERE id_supplier = ?';

        db.query(query, [supplierId], (err, results) => {
            if (err) {
                console.error('Error deleting supplier:', err);
                return res.status(500).json({ error: 'Error deleting supplier. This supplier might be referenced in purchase transactions.' });
            }

            res.json({
                message: 'Supplier deleted successfully',
                id: supplierId
            });
        });
    });
});

router.get('/:id/purchases', (req, res) => {
    const supplierId = req.params.id;
    const query = `
    SELECT h.nota_beli, h.subtotal_beli, h.id_karyawan, k.nama_karyawan, 
           d.id_produk, p.nama_produk, d.harga_beli, d.quantity
    FROM h_beli h
    JOIN d_beli d ON h.nota_beli = d.nota_beli
    JOIN karyawan k ON h.id_karyawan = k.id_karyawan
    JOIN produk p ON d.id_produk = p.id_produk
    WHERE h.id_supplier = ?
    ORDER BY h.nota_beli
  `;

    db.query(query, [supplierId], (err, results) => {
        if (err) {
            console.error('Error fetching supplier purchases:', err);
            return res.status(500).json({ error: 'Error fetching supplier purchases' });
        }

        const purchases = {};
        results.forEach(row => {
            if (!purchases[row.nota_beli]) {
                purchases[row.nota_beli] = {
                    nota_beli: row.nota_beli,
                    subtotal_beli: row.subtotal_beli,
                    id_karyawan: row.id_karyawan,
                    nama_karyawan: row.nama_karyawan,
                    items: []
                };
            }

            purchases[row.nota_beli].items.push({
                id_produk: row.id_produk,
                nama_produk: row.nama_produk,
                harga_beli: row.harga_beli,
                quantity: row.quantity
            });
        });

        res.json(Object.values(purchases));
    });
});

module.exports = router;