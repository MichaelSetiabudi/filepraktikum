import com.example.tugasm1.PlusUser
import com.example.tugasm1.StandardUser
import com.example.tugasm1.User
import java.util.Scanner
import kotlin.random.Random

fun main() {
    val scanner = Scanner(System.`in`)
    val users = mutableListOf<User>()
    var currentUser: User? = null

    while (true) {
        if (currentUser == null) {
            println("=== E-Wallet App ===")
            println("1. Login")
            println("2. Register")
            println("0. Exit")
            print("Pilihan: ")

            when (val choice = scanner.nextLine()) {
                "1" -> login(scanner, users)?.let { currentUser = it }
                "2" -> register(scanner, users)
                "0" -> {
                    println("Terima kasih telah menggunakan E-Wallet App")
                    return
                }
                else -> println("Pilihan tidak valid: $choice")
            }
        } else {
            println("=== User Menu ===")
            println("User: ${currentUser!!.name} (${currentUser!!.accountType})")
            println("Saldo: ${currentUser!!.balance}")
            println("Hutang: ${currentUser!!.loan}")
            println("1. Top-up")
            println("2. Transfer")
            println("3. Gacha")
            println("4. History Transaksi")
            println("5. Loan")
            println("0. Logout")

            print("Pilihan: ")
            val choice = scanner.nextLine()

            when (choice) {
                "1" -> topUp(scanner, currentUser!!)
                "2" -> transfer(scanner, currentUser!!, users)
                "3" -> gacha(scanner, currentUser!!)
                "4" -> currentUser!!.viewTransactionHistory()
                "5" -> loanMenu(scanner, currentUser!!)
                "0" -> {
                    println("Logout berhasil")
                    currentUser = null
                }
                else -> println("Pilihan tidak valid: $choice")
            }
        }
    }
}

fun register(scanner: Scanner, users: MutableList<User>): Boolean {
    println("=== Register ===")

    print("Nomor Telepon: ")
    val phoneNumber = scanner.nextLine()

    if (users.any { it.phoneNumber == phoneNumber }) {
        println("Nomor telepon sudah terdaftar")
        return false
    }

    print("Nama: ")
    val name = scanner.nextLine()
    if (name.isBlank()) {
        println("Nama tidak boleh kosong")
        return false
    }

    print("Password: ")
    val password = scanner.nextLine()
    if (password.isBlank()) {
        println("Password tidak boleh kosong")
        return false
    }

    print("Confirm Password: ")
    val confirmPassword = scanner.nextLine()
    if (password != confirmPassword) {
        println("Password tidak cocok")
        return false
    }

    print("Jenis Akun (STANDARD/PLUS): ")
    val accountType = scanner.nextLine().uppercase()

    if (accountType != "STANDARD" && accountType != "PLUS") {
        println("Jenis akun tidak valid")
        return false
    }

    val user = if (accountType == "STANDARD") {
        StandardUser(phoneNumber, name, password)
    } else {
        PlusUser(phoneNumber, name, password)
    }

    users.add(user)
    println("Registrasi berhasil! Silakan login")
    return true
}

fun login(scanner: Scanner, users: MutableList<User>): User? {
    println("=== Login ===")

    print("Nomor Telepon: ")
    val phoneNumber = scanner.nextLine()

    print("Password: ")
    val password = scanner.nextLine()

    val user = users.find { it.phoneNumber == phoneNumber && it.password == password }

    if (user == null) {
        println("Nomor telepon atau password salah")
        return null
    }

    println("Login berhasil!")
    return user
}

fun topUp(scanner: Scanner, user: User) {
    println("=== Top-up ===")

    print("Jumlah top-up (min. 10.000, kelipatan 10.000): ")
    val input = scanner.nextLine()

    try {
        val amount = input.toInt()
        user.topUp(amount)
    } catch (e: NumberFormatException) {
        println("Input tidak valid")
    }
}

fun transfer(scanner: Scanner, sender: User, users: MutableList<User>) {
    println("=== Transfer ===")

    print("Nomor telepon penerima: ")
    val recipientPhone = scanner.nextLine()

    val recipient = users.find { it.phoneNumber == recipientPhone }

    if (recipient == null) {
        println("Nomor telepon tidak terdaftar")
        return
    }

    if (recipient.phoneNumber == sender.phoneNumber) {
        println("Tidak dapat transfer ke diri sendiri")
        return
    }

    print("Jumlah transfer: ")
    val input = scanner.nextLine()

    try {
        val amount = input.toInt()
        sender.transfer(amount, recipient)
    } catch (e: NumberFormatException) {
        println("Input tidak valid")
    }
}

fun gacha(scanner: Scanner, user: User) {
    println("=== Gacha ===")
    println("Selamat datang di High or Low!")
    println("Tebak apakah angka berikutnya lebih tinggi atau lebih rendah")
    println("Minimal taruhan 10.000 dan harus kelipatan 10.000")
    println("Tekan 0 untuk kembali ke menu utama")

    print("Jumlah taruhan: ")
    val betInput = scanner.nextLine()

    if (betInput == "0") {
        return
    }

    try {
        val bet = betInput.toInt()

        if (!user.playGacha(bet)) {
            return
        }

        var currentBet = bet
        var playing = true

        while (playing) {
            var currentNumber = Random.nextInt(1, 11)
            println("Angka saat ini: $currentNumber")

            print("Tebakan (1: High, 2: Low, 0: Exit): ")
            val guess = scanner.nextLine()

            if (guess == "0") {
                return
            }

            if (guess != "1" && guess != "2") {
                println("Pilihan tidak valid")
                continue
            }

            var nextNumber: Int
            do {
                nextNumber = Random.nextInt(1, 11)
            } while (nextNumber == currentNumber)

            println("Angka berikutnya: $nextNumber")

            val isHigherGuess = guess == "1"
            val isHigher = nextNumber > currentNumber

            if ((isHigherGuess && isHigher) || (!isHigherGuess && !isHigher)) {
                user.balance += currentBet
                user.transactions.add("Gacha win +$currentBet : +$currentBet")
                println("Selamat! Anda menang! Saldo baru: ${user.balance}")
            } else {
                user.balance -= currentBet
                user.transactions.add("Gacha lost -$currentBet : -$currentBet")
                println("Maaf, Anda kalah. Saldo baru: ${user.balance}")
            }

            print("Ingin main lagi? (y/n): ")
            val playAgain = scanner.nextLine().lowercase()

            if (playAgain != "y") {
                playing = false
            } else {
                print("Jumlah taruhan: ")
                val newBetInput = scanner.nextLine()

                if (newBetInput == "0") {
                    return
                }

                try {
                    val newBet = newBetInput.toInt()
                    if (!user.playGacha(newBet)) {
                        return
                    }
                    currentBet = newBet
                } catch (e: NumberFormatException) {
                    println("Input tidak valid")
                    return
                }
            }
        }
    } catch (e: NumberFormatException) {
        println("Input tidak valid")
    }
}

fun loanMenu(scanner: Scanner, user: User) {
    println("=== Loan ===")
    println("1. Ambil Loan")
    println("2. Bayar Loan")
    println("0. Kembali")
    print("Pilihan: ")

    val choice = scanner.nextLine()

    when (choice) {
        "1" -> takeLoan(scanner, user)
        "2" -> repayLoan(scanner, user)
        "0" -> return
        else -> println("Pilihan tidak valid: $choice")
    }
}

fun takeLoan(scanner: Scanner, user: User) {
    if (!user.canTakeLoan()) {
        println("Anda masih memiliki hutang yang harus dibayar terlebih dahulu")
        return
    }

    println("=== Ambil Loan ===")
    println("Pilih opsi loan:")
    println("1. Rp.200.000 (Bunga 5%, Total Rp.210.000)")
    println("2. Rp.500.000 (Bunga 10%, Total Rp.550.000)")
    println("3. Rp.1.000.000 (Bunga 15%, Total Rp.1.150.000)")
    println("0. Kembali")
    print("Pilihan: ")

    val choice = scanner.nextLine()

    if (choice == "0") {
        return
    }

    val amount = when (choice) {
        "1" -> 200000
        "2" -> 500000
        "3" -> 1000000
        else -> {
            println("Pilihan tidak valid: $choice")
            return
        }
    }

    user.takeLoan(choice.toInt(), amount)
}

fun repayLoan(scanner: Scanner, user: User) {
    if (user.loan <= 0) {
        println("Anda tidak memiliki hutang")
        return
    }

    println("=== Bayar Loan ===")
    println("Hutang Anda: ${user.loan}")
    println("Saldo Anda: ${user.balance}")
    print("Jumlah pembayaran: ")

    val input = scanner.nextLine()

    try {
        val amount = input.toInt()
        user.repayLoan(amount)
    } catch (e: NumberFormatException) {
        println("Input tidak valid")
    }
}