<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
if(isset($_GET["ida"])) {
    $ida=$_GET["ida"];
    $q="DELETE FROM anketa WHERE idAnketa='".$ida."';";
    $qr=qq($q);
}