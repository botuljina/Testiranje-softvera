<?php
if (!isset($_SESSION))
    session_start();
if (!isset($_SESSION['tip']))
    header("location: login.php");
require_once 'header.php';
?>
    <br/>
    <h1 align='center'>Dobro došli na sajt!</h1>

<?php
require_once 'footer.php';
?>