<?php
session_start();
require_once 'header.php';


    if (isset($_SESSION['tip']))
    header("location: index.php");

require_once 'inc/database.php';
require_once 'inc/funkcije.php';
require_once 'inc/loginFunkcije.php';

if (isset($_GET['p'])){
    alert("Pogresan username ili password");
}
if(isset($_POST['logindugme'])){

    uloguj();

}
if(isset($_POST['regdugme'])){
    registruj();
}
if(isset($_POST['novalozdugme'])){
    promeniLozinku();
}


?>
<br>

<div class="izbor centar">
    <ul class="menu-area">
        <li class='fNav'><a href="#" onclick="showdiv('log', 'reg','novaloz')">Login</a></li>
        <li class='fNav'><a href="#" onclick="showdiv('reg', 'log','novaloz')">Registracija</a></li>
    </ul>
</div>
<br>
<div id="sve">
<div class="formaK" id="log">
    <form class="ui form"  name="formlogin" method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>" onsubmit="return proveraLogin();">

        <div class="field">
            <label>Username</label>
            <input type="text" name="username1" placeholder="Username" required>
        </div>
        <div class="field">
            <label>Password</label>
            <input type="password" name="password1" placeholder="Password" required>
        </div>

        <button class="ui button" name="logindugme" type="submit">Submit</button>
    </form>
    <br>
    <a class="loginlink" href="#" onclick="showdiv('novaloz','log','reg')" >Promeni lozinku!</a>
</div>


<div class="formaK" hidden id="reg">
    <form class="ui form "  name="formreg" method="POST" action="<?php $_SERVER["PHP_SELF"] ?>" onsubmit="return proveraReg();">


            <div class="field">
                <label>Username</label>
                <input type="text" name="username2" placeholder="Username" required>
            </div>
            <div class="field">
                <label>Lozinka</label>
                <input type="password" name="password2" placeholder="Password" required>
            </div>
            <div class="field">
                <label>Potvrda lozinke</label>
                <input type="password" name="password22" placeholder="Repeat password" required>
            </div>
            <div class="field">
                <label>Ime</label>
                <input type="text" name="ime" placeholder="Ime" required>
            </div>
            <div class="field">
                <label>Prezime</label>
                <input type="text" name="prezime" placeholder="Prezime" required>
            </div>
            <div class="field">
                <label>JMBG</label>
                <input type="text" name="jmbg" placeholder="JMBG" required>
            </div>
            <div class="field">
                <label>Datum rodjenja</label>
                <input type="date" name="datum" data-date-format="DD MM YYYY" placeholder="" required>
            </div>
            <div class="field">
                <label>Mesto rodjenja</label>
                <input type="text" name="mesto" placeholder="Mesto rodjenja" required>
            </div>
            <div class="field">
                <label>Kontakt telefon</label>
                <input type="text" name="telefon" placeholder="Telefon" required>
            </div>
            <div class="field">
                <label>Mejl</label>
                <input type="email" name="mejl" placeholder="Email" required>
            </div>
            <div class="field">
                <label>Tip korisnika:</label>
                <select name="tipkor">
                    <option value="1">Ispitanik</option>
                    <option value="2">Sluzbenik</option>
                    <option value="3">Autor</option>
                </select>
            </div>

        <button class="ui button" name="regdugme" type="submit">Submit</button>
        <br>
    </form>
    <br>
</div>
</div>

<div class="formaK" hidden id="novaloz">
    <form class="ui form"  name="formnew" method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>" onsubmit="return proveraNovaloz();">

        <div class="field">
            <label>Username</label>
            <input type="text" name="username3" placeholder="Username" required>
        </div>
        <div class="field">
            <label>Password</label>
            <input type="password" name="password3" placeholder="Password" required>
        </div>
        <div class="field">
            <label>New password</label>
            <input type="password" name="newpass" placeholder="" required>
        </div>
        <div class="field">
            <label>Confirm new password</label>
            <input type="password" name="newpass1" placeholder="" required>
        </div>

        <button class="ui button" name="novalozdugme" type="submit">Submit</button>
    </form>
    <br>
</div>