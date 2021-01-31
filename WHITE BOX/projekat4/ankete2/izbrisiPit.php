<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
if(isset($_GET["idpit"])) {
    $idpit=$_GET["idpit"];
    $q="DELETE FROM pitanja WHERE idPitanja='".$idpit."';";
    $qr=qq($q);
    header("location: autor.php");
}