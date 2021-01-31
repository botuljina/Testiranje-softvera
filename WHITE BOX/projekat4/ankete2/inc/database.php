<?php

$dbHost = 'localhost';
$dbUser = 'root';
$dbPass = '';
$dbName = 'ankete2';

$dbConn = new mysqli($dbHost, $dbUser, $dbPass, $dbName);
if (!$dbConn->set_charset("utf8")) {
    printf("Error loading character set utf8: %s\n", $dbConn->error);
    exit();
}
if ($dbConn->connect_error) {
    die("Greska u konekciji" . $dbConn->errno . ") " . $dbConn->error);
}

function sredi($p) {
    global $dbConn;
    return $dbConn->real_escape_string($p);
}

function qq($q) {
    global $dbConn;
    return $dbConn->query($q);
}

function kraj() {
    global $dbConn;
    $dbConn->close();
    require_once 'footer.php';
    exit();
}

?>