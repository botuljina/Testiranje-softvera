<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
if(isset($_GET["idpotp"])) {
    $idpotp=$_GET["idpotp"];
    $q="DELETE FROM potpitanja WHERE idPotpitanja='".$idpotp."';";
    $qr=qq($q);
    header("location: autor.php");
}