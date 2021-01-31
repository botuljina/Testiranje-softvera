<?php
require_once('inc/database.php');
if (!isset($_SESSION))session_start();

$q="SELECT * FROM ispitanik_anketa INNER JOIN anketa ON ispitanik_anketa.idAnketa=anketa.idAnketa WHERE ispitanik_anketa.username='".$_SESSION['kime']."';";

$qr=qq($q);
$qr=$qr->fetch_all(MYSQLI_ASSOC);
echo json_encode($qr);