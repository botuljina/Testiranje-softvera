<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');

if(isset($_GET["user"])) {
    $user = $_GET["user"];
    $q = "DELETE FROM korisnik WHERE username='" . $user . "';";
    $qr = qq($q);
}