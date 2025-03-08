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
        if (amount < 10000) {
            println("Top-up harus minimal 10.000")
            return
        }

        if (amount % 10000 != 0) {
            println("Top-up harus kelipatan 10.000")
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

        balance = balance - amount
        recipient.balance = recipient.balance + amount

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

        var i = 0
        for (transaction in transactions) {
            i = i + 1
            println("$i. $transaction")
        }
    }

    fun playGacha(bet: Int): Boolean {
        if (bet < 10000) {
            println("Minimal taruhan 10.000")
            return false
        }

        if (bet % 10000 != 0) {
            println("Taruhan harus kelipatan 10.000")
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

        var interestRate = 0.0
        if (option == 1) {
            interestRate = 0.05
        } else if (option == 2) {
            interestRate = 0.10
        } else if (option == 3) {
            interestRate = 0.15
        } else {
            println("Opsi loan tidak valid")
            return
        }

        val interest = (amount * interestRate).toInt()
        val totalRepayment = amount + interest

        balance = balance + amount
        loan = loan + totalRepayment

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

        balance = balance - amount
        loan = loan - amount

        transactions.add("Loan repayment $amount : - $amount")
        println("Pembayaran hutang berhasil! Saldo baru: $balance, Hutang: $loan")
    }
}