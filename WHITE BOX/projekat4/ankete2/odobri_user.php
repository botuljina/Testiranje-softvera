<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');

if(isset($_GET["user"])) {
    $user = $_GET["user"];
    $q = "UPDATE korisnik SET reg=1 WHERE username='" . $user . "';";
    $qr = qq($q);
}