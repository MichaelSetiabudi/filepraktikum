package com.example.tugasm1

abstract class User(
    val phoneNumber: String,
    val name: String,
    val password: String,
    var balance: Int = 0,
    var loan: Int = 0
) {
    val transactions = mutableListOf<String>()

    abstract val accountType: String

    fun topUp(amount: Int) {
        if (amount < 10000 || amount % 10000 != 0) {
            println("Top-up harus minimal 10.000 dan kelipatan 10.000")
            return
        }

        balance += amount
        transactions.add("Top-up $amount : + $amount")
        println("Top-up berhasil! Saldo baru: $balance")
    }

    fun transfer(amount: Int, recipient: User) {
        if (amount <= 0) {
            println("Jumlah transfer harus lebih dari 0")
            return
        }

        if (amount > balance) {
            println("Saldo tidak mencukupi")
            return
        }

        balance -= amount
        recipient.balance += amount

        transactions.add("Transferred $amount to ${recipient.name} : - $amount")
        recipient.transactions.add("Received $amount from $name : + $amount")

        println("Transfer berhasil! Saldo baru: $balance")
    }

    fun viewTransactionHistory() {
        if (transactions.isEmpty()) {
            println("Tidak ada transaksi")
            return
        }

        println("Transaction History:")
        transactions.forEachIndexed { index, transaction ->
            println("${index + 1}. $transaction")
        }
    }

    fun playGacha(bet: Int): Boolean {
        if (bet < 10000 || bet % 10000 != 0) {
            println("Minimal taruhan 10.000 dan harus kelipatan 10.000")
            return false
        }

        if (bet > balance) {
            println("Saldo tidak mencukupi")
            return false
        }

        return true
    }

    abstract fun canTakeLoan(): Boolean

    fun takeLoan(option: Int, amount: Int) {
        if (!canTakeLoan()) {
            println("Anda tidak dapat mengambil loan saat ini")
            return
        }

        val interestRate = when (option) {
            1 -> 0.05
            2 -> 0.10
            3 -> 0.15 
            else -> {
                println("Opsi loan tidak valid")
                return
            }
        }

        val interest = (amount * interestRate).toInt()
        val totalRepayment = amount + interest

        balance += amount
        loan += totalRepayment

        transactions.add("Loan received $amount (Repay: $totalRepayment) : + $amount")
        println("Loan berhasil! Saldo baru: $balance, Hutang: $loan")
    }

    fun repayLoan(amount: Int) {
        if (amount <= 0) {
            println("Jumlah pembayaran harus lebih dari 0")
            return
        }

        if (amount > balance) {
            println("Saldo tidak mencukupi")
            return
        }

        if (amount > loan) {
            println("Jumlah pembayaran melebihi hutang Anda")
            return
        }

        balance -= amount
        loan -= amount

        transactions.add("Loan repayment $amount : - $amount")
        println("Pembayaran hutang berhasil! Saldo baru: $balance, Hutang: $loan")
    }
}