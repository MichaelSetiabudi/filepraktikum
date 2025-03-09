import com.example.tugasm1.PlusUser
import com.example.tugasm1.StandardUser
import com.example.tugasm1.User
import kotlin.random.Random

fun main() {
    val users = mutableListOf<User>()
    var currentUser: User? = null

    while (true) {
        if (currentUser == null) {
            println("=== Main Menu ===")
            println("1. Login")
            println("2. Register")
            println("0. Exit")
            print("Choose an option: ")

            val choice = readLine()

            if (choice == "1") {
                val loggedInUser = login(users)
                if (loggedInUser != null) {
                    currentUser = loggedInUser
                }
            } else if (choice == "2") {
                register(users)
            } else if (choice == "0") {
                println("See You")
                return
            } else {
                println("Pilihan tidak valid: $choice")
            }
        } else {
            println("=== User Menu ===")
            println("Welcome, ${currentUser.name}")
            println("Saldo: Rp ${currentUser.balance}")
            println("Outstanding Loan: Rp ${currentUser.loan}")
            println("1. Top-up")
            println("2. Transfer")
            println("3. Gacha")
            println("4. View Transaction History")
            println("5. Loan (Pinjaman Uang)")
            println("6. Logout")

            print("Choose an option: ")
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
            } else if (choice == "6") {
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
    println("Registrasi berhasil!")
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

    print("Enter amount to top up (min. 10.000, multiple of 10.000): ")
    val input = readLine() ?: "0"

    val amount = input.toIntOrNull() ?: 0
    if (amount <= 0) {
        println("Input tidak valid")
        return
    }

    user.topUp(amount)
}

fun transfer(sender: User, users: MutableList<User>) {

    print("Enter recipient phone number: ")
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

    print("Enter amount to transfer: ")
    val input = readLine() ?: "0"

    val amount = input.toIntOrNull() ?: 0
    if (amount <= 0) {
        println("Input tidak valid")
        return
    }

    sender.transfer(amount, recipient)
}

fun gacha(user: User) {
    println("=== Gacha High-Low Game ===")
    println("You will bet an amount (min: 10,000, multiple of 10,000). If you win, you double your money.")

    var playing = true
    var saldo = user.balance

    while (playing) {
        print("Enter amount to bet (0 to exit): ")
        val betInput = readLine() ?: "0"

        if (betInput == "0") {
            return
        }

        val bet = betInput.toIntOrNull() ?: 0
        if (bet < 10000 || bet % 10000 != 0) {
            println("Invalid bet. Minimum bet is 10,000 and must be a multiple of 10,000.")
            continue
        }

        if (bet > saldo) {
            println("Insufficient balance. Your saldo is Rp $saldo")
            continue
        }

        val compareNumber = Random.nextInt(1, 11)
        print("Guess if the next number (1-10) will be (H)igher or (L)ower than $compareNumber: ")
        val guess = readLine()?.trim()?.uppercase() ?: ""

        if (guess != "H" && guess != "L") {
            println("Invalid choice. Please enter H for Higher or L for Lower.")
            continue
        }

        var nextNumber: Int
        do {
            nextNumber = Random.nextInt(1, 11)
        } while (nextNumber == compareNumber)

        println("The number drawn is: $nextNumber")

        val isHigherGuess = guess == "H"
        val isHigher = nextNumber > compareNumber

        if ((isHigherGuess && isHigher) || (!isHigherGuess && !isHigher)) {
            saldo += bet
            user.balance = saldo
            user.transactions.add("Gacha win +$bet : +$bet")
            println("? You won! Your saldo is now Rp $saldo")
        } else {
            saldo -= bet
            user.balance = saldo
            user.transactions.add("Gacha lost -$bet : -$bet")
            println("? You lost! Your saldo is now Rp $saldo")
        }

        if (saldo < 10000) {
            println("Your balance is too low to continue playing.")
            return
        }
    }
}


fun loanMenu(user: User) {
    println("=== Loan Menu(Pinjaman Uang) ===")
    println("1. Take Loan")
    println("2. Pay Loan")
    println("3. Back to User Menu")
    print("Choose an option: ")

    val choice = readLine() ?: "0"

    if (choice == "1") {
        takeLoan(user)
    } else if (choice == "2") {
        repayLoan(user)
    } else if (choice == "3") {
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

    println("=== Available Loan Option ===")
    println("Pilih opsi loan:")
    println("1. Rp. 1,000,000 with 10% interest (Total Repay: Rp. 1,100,000)")
    println("2. Rp. 5,000,000 with 7% interest (Total Repay: Rp. 5,350,000)")
    println("3. Rp. 10,000,000 with 5% interest (Total Repay: Rp. 10,500,000)")
    println("4. Cancel Loan request")
    print("Pilihan: ")

    val choice = readLine() ?: "0"

    if (choice == "4") {
        return
    }

    var amount = 0
    if (choice == "1") {
        amount = 1000000
    } else if (choice == "2") {
        amount = 5000000
    } else if (choice == "3") {
        amount = 10000000
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

    print("Enter amount to pay(or enter 0 to cancel): ")

    val input = readLine() ?: "0"
    if(input=="0"){
        return
    }
    val amount = input.toIntOrNull() ?: 0
    if (amount <= 0) {
        println("Input tidak valid")
        return
    }

    user.repayLoan(amount)
}