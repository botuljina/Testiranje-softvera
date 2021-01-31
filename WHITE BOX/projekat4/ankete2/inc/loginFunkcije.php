<?php


function alert($msg)
{
    echo "<script type='text/javascript'>alert('$msg');</script>";
}

function uloguj() {
    if (!isset($_POST['logdugme']))
        header('location:login.php');
    $un = sredi($_POST['username1']);
    $pa = sredi($_POST['password1']);
    $pa=md5($pa);
    $q = "SELECT * FROM korisnik WHERE username='" . $un . "' AND password='" . $pa . "'";
    $qr = qq($q);
echo $qr->num_rows;

    if ($qr->num_rows == 1) {
        //alert(1);
        $n = $qr->fetch_assoc();
        if($n['reg']==0){session_destroy();header("location:index.php");exit();}
        $_SESSION['kime'] = $n['username'];
        $_SESSION['tip'] = $n['tip'];
        if ($n['tip'] == 1) {
            // punis session['ime_korisnika'] iz tabele osoba
            $q = "SELECT * FROM ispitanik WHERE username='" . $un . "' AND password='" . $pa . "'";
            $qr = qq($q);
            if ($qr->num_rows == 1) {
                $niz = $qr->fetch_assoc();
                $_SESSION['korisnik'] = "Ispitanik: ".$niz['ime']." ".$niz['prezime'];
            }
        }
        if ($n['tip'] == 2) {
            // punis session['ime_komp'] iz tabele kompanija
            $q = "SELECT * FROM sluzbenik WHERE username='" . $un . "' AND password='" . $pa . "'";
            $qr = qq($q);
            if ($qr->num_rows == 1) {
                $niz = $qr->fetch_assoc();
                $_SESSION['korisnik'] = "Sluzbenik: ".$niz['ime']." ".$niz['prezime'];
            }
        }
        if ($n['tip'] == 3) {
//            $_SESSION['korisnik'] = "Administrator";
//            $_SESSION['slika'] = "img/studenti/admin.png";
            $q = "SELECT * FROM autor WHERE username='" . $un . "' AND password='" . $pa . "'";
            $qr = qq($q);
            if ($qr->num_rows == 1) {
                $niz = $qr->fetch_assoc();
                $_SESSION['korisnik'] = "Autor: ".$niz['ime']." ".$niz['prezime'];
            }
        }
        if ($n['tip'] == 4) {
            $_SESSION['korisnik'] = "Administrator";
        }
        header('location: index.php');
    } else {
        header("location: login.php?p");

        //greska("Pogrešan username ili šifra");
        
    }
}
function registruj(){
    if (!isset($_POST['regdugme']))
        header("login.php");

    $un = sredi($_POST['username2']);
    $pa = sredi($_POST['password2']);
    $im = sredi($_POST['ime']);
    $pr = sredi($_POST['prezime']);
    $jmbg = sredi($_POST['jmbg']);
    $m = sredi($_POST['mejl']);
    $tel = sredi($_POST['telefon']);
    $dat = sredi($_POST['datum']);
    $mesto = sredi($_POST['mesto']);
    $tk = sredi($_POST['tipkor']);

    $q = "SELECT * FROM korisnik WHERE username='" . $un . "';";
    $qr = qq($q);

    $greska = '';
    if ($qr->num_rows > 0) {
        $greska = "Takav korisnik već postoji!<br/>";
        alert($greska);
    } else {

        $pa = md5($pa);
        $q = "INSERT INTO korisnik (username, password, email, tip,reg) VALUES ('" . $un . "','" . $pa . "','" . $m . "','" . $tk ."',0);";
        $qr1 = qq($q);
        $qr2='';
        if($tk==1) {
            $q = "INSERT INTO ispitanik (username, ime, prezime, jmbg,datum_rodjenja,mesto_rodjenja,telefon) VALUES ('" . $un . "','" . $im . "','" . $pr . "','" . $jmbg . "','" . $dat . "','" . $mesto . "','" . $tel . "');";
            $qr2 = qq($q);
        }elseif($tk==2) {
            $q = "INSERT INTO sluzbenik (username, ime, prezime, jmbg,datum_rodjenja,mesto_rodjenja,telefon) VALUES ('" . $un . "','" . $im . "','" . $pr . "','" . $jmbg . "','" . $dat . "','" . $mesto . "','" . $tel . "');";
            $qr2 = qq($q);
        }elseif($tk==3) {
            $q = "INSERT INTO autor (username, ime, prezime, jmbg,datum_rodjenja,mesto_rodjenja,telefon) VALUES ('" . $un . "','" . $im . "','" . $pr . "','" . $jmbg . "','" . $dat . "','" . $mesto . "','" . $tel . "');";
            $qr2 = qq($q);
        }

        if ($qr1 && $qr2) {

            $poruka = "Uspešno ste se registrovali!";
            alert($poruka);
            $_SESSION['kime'] = $un;
            $_SESSION['tip'] = $tk;
            header("Index.php");
        } else {
            if (!isset($greska))
                $greska = '';
            $greska = $greska . "Došlo je do greške!<br/>";
        }
    }
}

function promeniLozinku(){
    if (!isset($_POST['novalozdugme']))
        header("login.php");

    $un = sredi($_POST['username3']);
    $pa = sredi($_POST['password3']);
    $nl = sredi($_POST['newpass']);
    $nl2 = sredi($_POST['newpass1']);

    $pa = md5($pa);
    $q = "SELECT * FROM korisnik WHERE username='" . $un . "' AND password='" . $pa . "'";
    $qr = qq($q);
    $greska = '';
    if ($qr->num_rows == 1) {
        $n = $qr->fetch_assoc();
        if ($nl !== $nl2) {
            $greska = "Lozinke moraju biti iste!";
            alert($greska);
        } else {
            $nl = md5($nl);
            $user = $n['username'];
            $q = "UPDATE korisnik SET password='" . $nl . "' WHERE username='" . $user . "';";
            $qr = qq($q);
            if ($qr) {
                $_SESSION['kime'] = $n['username'];
                $_SESSION['tip'] = $n['tip'];
                $por = "Uspesno promenjena lozinka!";
                alert($por);
                header("Index.php");
            } else {
                $greska = "Neuspesno promenjena lozinka!";
                alert($greska);
            }
        }
    } else {
        $greska = "Pogresan username ili lozinka!";
        alert($greska);
    }
}

?>