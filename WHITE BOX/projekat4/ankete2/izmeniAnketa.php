<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');


if(isset($_GET["ida"])) {
    $ida = $_GET["ida"];
    $q = "SELECT * FROM anketa_pitanja  INNER JOIN pitanja 
                ON anketa_pitanja.idPitanja =pitanja.idPitanja
                WHERE anketa_pitanja.idAnketa='" . $ida . "' ORDER BY red";
    $qr = qq($q);
    $qr = $qr->fetch_all(MYSQLI_ASSOC);
    echo json_encode($qr);
}



