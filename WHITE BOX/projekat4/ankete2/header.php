
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="Semantic/semantic.min.css">
        <link rel="stylesheet" type="text/css" href="stilovi1/stilovi1.css">
<!--        <link rel="icon" href="img/jf19icon.ico">-->
        <title>Anketa</title>
        <script
            src="funkcijee/jquery-3.1.1.min.js"
            integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="
        crossorigin="anonymous"></script>
        <script src="Semantic/semantic.min.js"></script>
        <script type="text/javascript" src="funkcijee/login.js"></script>
        <script type="text/javascript" src="funkcijee/jsfunkcije.js"></script>

    </head>

    <body>
        <div class="container">
            <div class="custom-padding">
                <nav>
                    <div class="logo"><a href="index.php"><img style="height:30px" src="img/anketalogo.png"></a></div>

                    <ul class="menu-area">
                        <li><a href="index.php">Pocetna</a></li>
                        <?php
                        if (!isset($_SESSION))
                            session_start();

                        if (isset($_SESSION['tip'])) {
                            require_once 'meniFunkcije.php';
                            if ($_SESSION['tip'] == 1)
                                meniIsp();
                            elseif ($_SESSION['tip'] == 2)
                                meniSl();
                            elseif ($_SESSION['tip'] == 3)
                                meniAutor();
                            elseif ($_SESSION['tip'] == 4)
                                meniAdmin();
                            ?>

                            <li><a href="logout.php">LOGOUT</a></li>

                            <?php
                        }
                        ?>
                    </ul>
                </nav>
            </div>
            <br>
            <div class="mainWrap">


            <div class='greskaP hidden' id='greskaPoruka'>
                <div class='greskaS' id='porukaSadrzaj'></div>
            </div>