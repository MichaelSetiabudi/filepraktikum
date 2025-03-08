import com.example.tugasm1.PlusUser
import com.example.tugasm1.StandardUser
import com.example.tugasm1.User
import kotlin.random.Random

fun main() {
    val users = mutableListOf<User>()
    var currentUser: User? = null

    while (true) {
        if (currentUser == null) {
            println("=== E-Wallet App ===")
            println("1. Login")
            println("2. Register")
            println("0. Exit")
            print("Pilihan: ")

            val choice = readLine()

            if (choice == "1") {
                val loggedInUser = login(users)
                if (loggedInUser != null) {
                    currentUser = loggedInUser
                }
            } else if (choice == "2") {
                register(users)
            } else if (choice == "0") {
                println("Terima kasih telah menggunakan E-Wallet App")
                return
            } else {
                println("Pilihan tidak valid: $choice")
            }
        } else {
            println("=== User Menu ===")
            println("User: ${currentUser.name} (${currentUser.accountType})")
            println("Saldo: ${currentUser.balance}")
            println("Hutang: ${currentUser.loan}")
            println("1. Top-up")
            println("2. Transfer")
            println("3. Gacha")
            println("4. History Transaksi")
            println("5. Loan")
            println("0. Logout")

            print("Pilihan: ")
            val choice = readLine()

            if (choice == "1") {
                topUp(currentUser)
            } else if (choice == "2") {
                transfer(currentUser, users)
            } else if (choice == "3") {
                gacha(currentUser)
            } else if (choice == "4") {
                currentUser.viewTransactionHistory()
            } else if (choice == "5") {
                loanMenu(currentUser)
            } else if (choice == "0") {
                println("Logout berhasil")
                currentUser = null
            } else {
                println("Pilihan tidak valid: $choice")
            }
        }
    }
}

fun register(users: MutableList<User>): Boolean {
    println("=== Register ===")

    print("Nomor Telepon: ")
    val phoneNumber = readLine() ?: ""

    for (existingUser in users) {
        if (existingUser.phoneNumber == phoneNumber) {
            println("Nomor telepon sudah terdaftar")
            return false
        }
    }

    print("Nama: ")
    val name = readLine() ?: ""
    if (name.isBlank()) {
        println("Nama tidak boleh kosong")
        return false
    }

    print("Password: ")
    val password = readLine() ?: ""
    if (password.isBlank()) {
        println("Password tidak boleh kosong")
        return false
    }

    print("Confirm Password: ")
    val confirmPassword = readLine() ?: ""
    if (password != confirmPassword) {
        println("Password tidak cocok")
        return false
    }

    print("Jenis Akun (STANDARD/PLUS): ")
    val accountType = (readLine() ?: "").uppercase()

    if (accountType != "STANDARD" && accountType != "PLUS") {
        println("Jenis akun tidak valid")
        return false
    }

    val user: User
    if (accountType == "STANDARD") {
        user = StandardUser(phoneNumber, name, password)
    } else {
        user = PlusUser(phoneNumber, name, password)
    }

    users.add(user)
    println("Registrasi berhasil! Silakan login")
    return true
}

fun login(users: MutableList<User>): User? {
    println("=== Login ===")

    print("Nomor Telepon: ")
    val phoneNumber = readLine() ?: ""

    print("Password: ")
    val password = readLine() ?: ""

    var foundUser: User? = null
    for (user in users) {
        if (user.phoneNumber == phoneNumber && user.password == password) {
            foundUser = user
            break
        }
    }

    if (foundUser == null) {
        println("Nomor telepon atau password salah")
        return null
    }

    println("Login berhasil!")
    return foundUser
}

fun topUp(user: User) {
    println("=== Top-up ===")

    print("Jumlah top-up (min. 10.000, kelipatan 10.000): ")
    val input = readLine() ?: "0"

    val amount = input.toIntOrNull() ?: 0
    if (amount <= 0) {
        println("Input tidak valid")
        return
    }

    user.topUp(amount)
}

fun transfer(sender: User, users: MutableList<User>) {
    println("=== Transfer ===")

    print("Nomor telepon penerima: ")
    val recipientPhone = readLine() ?: ""

    var recipient: User? = null
    for (user in users) {
        if (user.phoneNumber == recipientPhone) {
            recipient = user
            break
        }
    }

    if (recipient == null) {
        println("Nomor telepon tidak terdaftar")
        return
    }

    if (recipient.phoneNumber == sender.phoneNumber) {
        println("Tidak dapat transfer ke diri sendiri")
        return
    }

    print("Jumlah transfer: ")
    val input = readLine() ?: "0"

    val amount = input.toIntOrNull() ?: 0
    if (amount <= 0) {
        println("Input tidak valid")
        return
    }

    sender.transfer(amount, recipient)
}

fun gacha(user: User) {
    println("=== Gacha ===")
    println("Selamat datang di High or Low!")
    println("Tebak apakah angka berikutnya lebih tinggi atau lebih rendah")
    println("Minimal taruhan 10.000 dan harus kelipatan 10.000")
    println("Tekan 0 untuk kembali ke menu utama")

    print("Jumlah taruhan: ")
    val betInput = readLine() ?: "0"

    if (betInput == "0") {
        return
    }

    val bet = betInput.toIntOrNull() ?: 0
    if (bet <= 0) {
        println("Input tidak valid")
        return
    }

    if (!user.playGacha(bet)) {
        return
    }

    var currentBet = bet
    var playing = true

    while (playing) {
        var currentNumber = Random.nextInt(1, 11)
        println("Angka saat ini: $currentNumber")

        print("Tebakan (1: High, 2: Low, 0: Exit): ")
        val guess = readLine() ?: "0"

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
        val playAgain = (readLine() ?: "n").lowercase()

        if (playAgain != "y") {
            playing = false
        } else {
            print("Jumlah taruhan: ")
            val newBetInput = readLine() ?: "0"

            if (newBetInput == "0") {
                return
            }

            val newBet = newBetInput.toIntOrNull() ?: 0
            if (newBet <= 0) {
                println("Input tidak valid")
                return
            }

            if (!user.playGacha(newBet)) {
                return
            }
            currentBet = newBet
        }
    }
}

fun loanMenu(user: User) {
    println("=== Loan ===")
    println("1. Ambil Loan")
    println("2. Bayar Loan")
    println("0. Kembali")
    print("Pilihan: ")

    val choice = readLine() ?: "0"

    if (choice == "1") {
        takeLoan(user)
    } else if (choice == "2") {
        repayLoan(user)
    } else if (choice == "0") {
        return
    } else {
        println("Pilihan tidak valid: $choice")
    }
}

fun takeLoan(user: User) {
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

    val choice = readLine() ?: "0"

    if (choice == "0") {
        return
    }

    var amount = 0
    if (choice == "1") {
        amount = 200000
    } else if (choice == "2") {
        amount = 500000
    } else if (choice == "3") {
        amount = 1000000
    } else {
        println("Pilihan tidak valid: $choice")
        return
    }

    val choiceNum = choice.toIntOrNull() ?: 0
    user.takeLoan(choiceNum, amount)
}

fun repayLoan(user: User) {
    if (user.loan <= 0) {
        println("Anda tidak memiliki hutang")
        return
    }

    println("=== Bayar Loan ===")
    println("Hutang Anda: ${user.loan}")
    println("Saldo Anda: ${user.balance}")
    print("Jumlah pembayaran: ")

    val input = readLine() ?: "0"

    val amount = input.toIntOrNull() ?: 0
    if (amount <= 0) {
        println("Input tidak valid")
        return
    }

    user.repayLoan(amount)
}