<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
if(isset($_GET["idp"])) {
    $idp = $_GET["idp"];
    $q="SELECT * FROM potpitanja WHERE idPitanja='".$idp."';";
    $qr=qq($q);
    $qr = $qr->fetch_all(MYSQLI_ASSOC);
    echo json_encode($qr);
}