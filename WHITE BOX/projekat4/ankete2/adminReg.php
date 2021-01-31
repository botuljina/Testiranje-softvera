<?php
if (!isset($_SESSION))session_start();
require_once('inc/database.php');
require_once("header.php");

$q="SELECT * FROM korisnik WHERE reg=0;";
$qr=qq($q);
    ?>
    <table class="ui celled  table " id="regtable">
        <thead>
        <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Tip korisnika</th>
            <th>Zahtev</th>
        </tr>
        </thead>
        <tbody>
    <?php
while($n=$qr->fetch_assoc()){
    if($n['tip']!=4) {
        ?>
        <tr>
            <td><?php echo $n['username']; ?></td>
            <td><?php echo $n['email']; ?></td>
            <td><?php if ($n['tip'] == 1) {
                    echo "Ispitanik";
                } elseif ($n['tip'] == 2) {
                    echo "Sluzbenik";
                } elseif ($n['tip'] == 3) {
                    echo "Autor";
                }
                ?></td>
            <td>
                <button class="ui button odobridugme" value="<?php echo $n['username']; ?>" type="button">ODOBRI
                </button>
                &nbsp;<button class="ui button odbijdugme" id="" value="<?php echo $n['username']; ?>" type="button">ODBIJ</button>
            </td>
        </tr>
        <?php
    }
}
?>
    <script>
        $('.odobridugme').click(function(){
            var user=$(this).val();
            $(this).parent().parent().remove();

            $.get('odobri_user.php', { user: user}, function(data){

            });
        });


        $('.odbijdugme').click(function(){
            var user=$(this).val();
            $(this).parent().parent().remove();

            $.get('izbrisi_korisnik.php', { user: user}, function(data){

            });
        });
    </script>
