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
    const query = `
    SELECT p.*, k.nama_kategori, j.nama_jenis, m.nama_merk
    FROM produk p
    LEFT JOIN kategori k ON p.id_kategori = k.id_kategori
    LEFT JOIN jenis j ON p.id_jenis = j.id_jenis
    LEFT JOIN merk m ON p.id_merk = m.id_merk
  `;

    db.query(query, (err, results) => {
        if (err) {
            console.error('Error fetching products:', err);
            return res.status(500).json({ error: 'Database error' });
        }
        res.json(results);
    });
});

router.get('/:id', (req, res) => {
    const productId = req.params.id;
    const query = `
    SELECT p.*, k.nama_kategori, j.nama_jenis, m.nama_merk,
           (SELECT AVG(rating) FROM rating_produk WHERE id_produk = p.id_produk) as avg_rating
    FROM produk p
    LEFT JOIN kategori k ON p.id_kategori = k.id_kategori
    LEFT JOIN jenis j ON p.id_jenis = j.id_jenis
    LEFT JOIN merk m ON p.id_merk = m.id_merk
    WHERE p.id_produk = ?
  `;

    db.query(query, [productId], (err, results) => {
        if (err) {
            console.error('Error fetching product:', err);
            return res.status(500).json({ error: 'Database error' });
        }

        if (results.length === 0) {
            return res.status(404).json({ error: 'Product not found' });
        }

        res.json(results[0]);
    });
});

router.get('/category/:categoryId', (req, res) => {
    const categoryId = req.params.categoryId;
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
            return res.status(500).json({ error: 'Database error' });
        }

        res.json(results);
    });
});

router.get('/brand/:brandId', (req, res) => {
    const brandId = req.params.brandId;
    const query = `
    SELECT p.*, k.nama_kategori, j.nama_jenis, m.nama_merk
    FROM produk p
    LEFT JOIN kategori k ON p.id_kategori = k.id_kategori
    LEFT JOIN jenis j ON p.id_jenis = j.id_jenis
    LEFT JOIN merk m ON p.id_merk = m.id_merk
    WHERE p.id_merk = ?
  `;

    db.query(query, [brandId], (err, results) => {
        if (err) {
            console.error('Error fetching products by brand:', err);
            return res.status(500).json({ error: 'Database error' });
        }

        res.json(results);
    });
});

router.post('/', (req, res) => {
    const { id_produk, nama_produk, id_kategori, id_jenis, id_merk, stok, harga } = req.body;

    if (!id_produk || !nama_produk || !id_kategori || !harga) {
        return res.status(400).json({ error: 'Required fields: id_produk, nama_produk, id_kategori, harga' });
    }

    const query = 'INSERT INTO produk (id_produk, nama_produk, id_kategori, id_jenis, id_merk, stok, harga) VALUES (?, ?, ?, ?, ?, ?, ?)';

    db.query(query, [id_produk, nama_produk, id_kategori, id_jenis, id_merk, stok || 0, harga], (err, result) => {
        if (err) {
            console.error('Error creating product:', err);
            return res.status(500).json({ error: 'Database error' });
        }

        res.status(201).json({
            message: 'Product created successfully',
            id: id_produk
        });
    });
});

router.put('/:id', (req, res) => {
    const productId = req.params.id;
    const { nama_produk, id_kategori, id_jenis, id_merk, stok, harga } = req.body;

    if (!nama_produk || !id_kategori || !harga) {
        return res.status(400).json({ error: 'Required fields: nama_produk, id_kategori, harga' });
    }

    const query = 'UPDATE produk SET nama_produk = ?, id_kategori = ?, id_jenis = ?, id_merk = ?, stok = ?, harga = ? WHERE id_produk = ?';

    db.query(query, [nama_produk, id_kategori, id_jenis, id_merk, stok, harga, productId], (err, result) => {
        if (err) {
            console.error('Error updating product:', err);
            return res.status(500).json({ error: 'Database error' });
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Product not found' });
        }

        res.json({ message: 'Product updated successfully' });
    });
});

router.patch('/:id/stock', (req, res) => {
    const productId = req.params.id;
    const { stok } = req.body;

    if (stok === undefined) {
        return res.status(400).json({ error: 'Stock value is required' });
    }

    const query = 'UPDATE produk SET stok = ? WHERE id_produk = ?';

    db.query(query, [stok, productId], (err, result) => {
        if (err) {
            console.error('Error updating product stock:', err);
            return res.status(500).json({ error: 'Database error' });
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Product not found' });
        }

        res.json({ message: 'Product stock updated successfully' });
    });
});
router.delete('/:id', (req, res) => {
    const productId = req.params.id;
    const query = 'DELETE FROM produk WHERE id_produk = ?';

    db.query(query, [productId], (err, result) => {
        if (err) {
            console.error('Error deleting product:', err);
            return res.status(500).json({ error: 'Database error' });
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({ error: 'Product not found' });
        }

        res.json({ message: 'Product deleted successfully' });
    });
});

router.get('/:id/ratings', (req, res) => {
    const productId = req.params.id;
    const query = `
    SELECT r.rating, c.nama_customer, h.nota_jual
    FROM rating_produk r
    JOIN h_jual h ON r.nota_jual = h.nota_jual
    JOIN customer c ON h.id_customer = c.id_customer
    WHERE r.id_produk = ?
  `;

    db.query(query, [productId], (err, results) => {
        if (err) {
            console.error('Error fetching product ratings:', err);
            return res.status(500).json({ error: 'Database error' });
        }

        const avgRating = results.reduce((sum, item) => sum + item.rating, 0) / (results.length || 1);

        res.json({
            product_id: productId,
            ratings: results,
            average_rating: parseFloat(avgRating.toFixed(1)),
            total_ratings: results.length
        });
    });
});

router.get('/search/:keyword', (req, res) => {
    const keyword = `%${req.params.keyword}%`;
    const query = `
    SELECT p.*, k.nama_kategori, j.nama_jenis, m.nama_merk
    FROM produk p
    LEFT JOIN kategori k ON p.id_kategori = k.id_kategori
    LEFT JOIN jenis j ON p.id_jenis = j.id_jenis
    LEFT JOIN merk m ON p.id_merk = m.id_merk
    WHERE p.nama_produk LIKE ? OR k.nama_kategori LIKE ? OR m.nama_merk LIKE ?
  `;

    db.query(query, [keyword, keyword, keyword], (err, results) => {
        if (err) {
            console.error('Error searching products:', err);
            return res.status(500).json({ error: 'Database error' });
        }

        res.json(results);
    });
});

module.exports = router;