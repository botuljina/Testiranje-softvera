<?php
require_once 'inc/database.php';
require_once 'header.php';
require_once 'inc/submit_anketa.php';
require_once "brisiAnketa.php";
require_once "izmeniAnketa.php";
require_once "izmeniPitanje.php";
require_once "izmeniPotp.php";

if (!isset($_SESSION))
    session_start();

if(!isset($_POST['nadugme'])){
    prikazi_izbor();
}else{
//    prikazi_pitanje();
}

function prikazi_izbor(){

    if(!isset($_GET['id'])){
       ?>
    <div class="ui list izb">
        <a class="item" href="autor.php?id=1">Napravi novu anketu</a>
        <a class="item"  href="autor.php?id=2">Pregledaj tvoje ankete</a>
        <a class="item"  href="autor.php?id=3">Izmeni anketu</a>
        <a class="item"  href="autor.php?id=4">Izbrisi anketu</a>
    </div>

<?php
    }
    else{
        autorfunkcije($_GET['id']);
    }
}
function autorfunkcije($id){
    if($id==1){?>

        <div id="tippit" class="hidden">
            <label for="tipp">Izaberite tip pitanja koji dodajete:</label>
            <select class="ui dropdown" id="tipp">
                <option value="-1"></option>
                <option value="1">Slobodan unos numericke vrednosti</option>
                <option value="2">Slobodan unos kratkog teksta</option>
                <option value="3">Slobodan unos dugackog teksta</option>
                <option value="4">Padajuca lista(jedan odgovor)</option>
                <option value="5">Cekboksovi</option>
                <option value="6">Matrica odgovora(1 odgovor po redu)</option>
                <option value="7">Matrica odgovora(vise odgovora po redu)</option>
            </select>
        </div>
        <div id="naform">

        <form class="ui form"  id="naform" name="naform" method="POST" action="<?php echo $_SERVER['PHP_SELF']; ?>" onsubmit="">
            <div id="ankdiv">
            <div class="field">
                <label>Naziv ankete</label>
                <input type="text" name="anknaziv" placeholder="" required value="Anketa 1">
            </div>
            <div class="field">
                <label>Datum pocetka</label>
                <input type="date" name="datpoc" placeholder="" required value="2019-01-01">
            </div>
            <div class="field">
                <label>Datum kraja</label>
                <input type="date" name="datkr" placeholder="" required value="2020-01-01">
            </div>
            <div class="field">
                <label>Anonimna?</label>
                <input type="radio" name="an" value="0" checked>NE &nbsp;
                <input type="radio" name="an" value="1">DA
            </div>
            </div>

            <div id="pitanje1" class="hidden">
                <div id="pit1div">
                <div class="field">

                    <label>Pitanje:</label>
                    <input type="text" name="pit1" id="pit1" placeholder="" required>
                </div>
                <div class="field potpitanje_1">
                    <label>Potpitanje:</label>
                    <input type="text" name="potp[]" class="potpValue" placeholder="" required>
                </div>
                </div>
                <button class="ui button" onclick="dodaj_potpitanje1()" type="button" >Dodaj</button>
                <button class="ui button hidden" style="display:none;" id="oduzmidugme1" onclick="oduzmi_potpitanje1()" type="button" >Oduzmi</button>
                <div id="divizbor">
                <div class="field izbor_1">
                    <label>Izbor:</label>
                    <input type="text" name="izbor[]" class="izbValue"  placeholder="">
                </div>
                    <button class="ui button " onclick="dodaj_izbor1()" type="button" >Dodaj</button>
                    <button class="ui button hidden" style="display:none;" id="oduzmidugme2" onclick="oduzmi_izbor1()" type="button" >Oduzmi</button>
                </div>

                <div class="field">
                    <label>Obavezno?</label>
                    <input type="radio" name="ob1" value="0">NE &nbsp;
                    <input type="radio" name="ob1" value="1">DA
                </div>
            </div>
            <div class="hidden field" id="brstran"> </div>
            <br>
            <button class="ui button" id="nadugme" name="nadugme" type="button" onclick="nastavi()">NASTAVI</button>
            <br>

            <div class="hidden" id="divdugme">
                <br>
            <button class="ui primary button" id="anksubmit" name="anksubmit" type="button" onclick="nova_anketa()">KREIRAJ</button>
            </div>
        </form>
        </div>
        <br>

    <?php }
    elseif ($id==2){
        $q="SELECT * FROM anketa WHERE autor='".$_SESSION['kime']."';";
        $qr=qq($q);
        ?>
<label> Pregled mojih anketa:</label>
<table class="ui celled  table " id="pregled">
    <thead>
    <tr>
        <th>Anketa</th>
        <th>Datum pocetka</th>
        <th>Datum kraja</th>
    </tr>
    </thead>

    <tbody>
    <?php
if($qr->num_rows>0){
    while($n=$qr->fetch_assoc()){
        ?>
<tr>
    <td><a class="linkdugme" ida='<?php echo $n['idAnketa']; ?>'><?php echo $n['naziv']; ?></a></td>
    <td><?php echo $n['datum_pocetka']; ?></td>
    <td><?php echo $n['datum_kraja']; ?></td>
</tr>
<?php
    }
}
?>

    </tbody>
    </table>
    <?php
    }elseif($id==3){
        $q="SELECT * FROM anketa WHERE autor='".$_SESSION['kime']."';";
        $qr=qq($q);
        ?>
        <table class="ui celled table " id="promenaTab">
            <thead>
            <tr>
                <th>Anketa</th>
                <th>Datum pocetka</th>
                <th>Datum kraja</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <?php
            if($qr->num_rows>0){
                while($n=$qr->fetch_assoc()){
                    ?>
                    <tr>
                        <td><a class='izmenidugme' ida="<?php echo $n['idAnketa']; ?>"><?php echo $n['naziv']; ?></a></td>
                        <td><?php echo $n['datum_pocetka']; ?></td>
                        <td><?php echo $n['datum_kraja']; ?></td>
<!--                        <td><button class="ui button "  value='--><?php //echo $n['idAnketa']; ?><!--' >IZMENI</button></td>-->
                        <td><button class="ui button "  value='<?php echo $n['idAnketa']; ?>' onclick="redosled(this.value)">PROMENA REDOSLEDA PITANJA</button></td>
                    </tr>
                    <?php
                }
            }
            ?>
            </tbody>
        </table>
        <br>
        <label class="ui label" style="font-size: 15px;">Unesi u anketu vec postojece pitanje:</label>
        <br>
        <select class="ui dropdown" id="ankselect">
            <option value="-1"></option>
            <?php
            $q="SELECT * FROM anketa";
            $qr=qq($q);
            while($n=$qr->fetch_assoc()){
                ?>
                <option value="<?php echo $n['idAnketa']; ?>"><?php echo $n['naziv'];?></option>
                <?php
            }
            ?>
        </select>&nbsp;&nbsp;
        <select class="ui dropdown" id="pitselect">
            <option value="-1"></option>
            <?php
            $q="SELECT * FROM pitanja";
            $qr=qq($q);
            while($n=$qr->fetch_assoc()){
        ?>
        <option value="<?php echo $n['idPitanja']; ?>"><?php echo $n['tekst'];?></option>
                <br>

        <?php
    }
    ?>
        </select><br><br>
            <button type="button" class="ui primary button" onclick="ubaci()">UBACI</button>
        <?php
    }
    elseif($id==4){
        $q="SELECT * FROM anketa WHERE autor='".$_SESSION['kime']."';";
        $qr=qq($q);
        ?>
        <table class="ui celled  table " id="brisanje">
            <thead>
            <tr>
                <th>Anketa</th>
                <th>Datum pocetka</th>
                <th>Datum kraja</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <?php
            if($qr->num_rows>0){
                while($n=$qr->fetch_assoc()){
                    ?>
                    <tr>
                        <td><?php echo $n['naziv']; ?></td>
                        <td><?php echo $n['datum_pocetka']; ?></td>
                        <td><?php echo $n['datum_kraja']; ?></td>
                        <td><button class="ui button brisidugme"  value='<?php echo $n['idAnketa']; ?>' >IZBRISI</button></td>
                    </tr>
                    <?php
                }
            }
            ?>
            </tbody>
        </table>
        <?php
    }
    }


?>
<script>
    ii=-1;
    br_potpitanja_1=1;
    br_izbora=1;
    function dodaj_potpitanje1(){
        var potp=document.getElementsByClassName('potpitanje_1');
       // console.log(potp);
        var htmltext='<div class=\"field potpitanje_1\" >\n'+
            '<label>Potpitanje:</label>\n'  +
            '<input type=\"text\" name=\"potp[]\" class=\"potpValue\" placeholder=\"\" required>\n'  +
             '</div>';
       // console.log(potp);

        potp=potp[br_potpitanja_1-1];
        potp.insertAdjacentHTML('afterend', htmltext);
        br_potpitanja_1++;
        if(br_potpitanja_1>1){document.getElementById('oduzmidugme1').style.display='inline';}


    }
    function oduzmi_potpitanje1 (){
        if(br_potpitanja_1==1)return;
        br_potpitanja_1--;
        if(br_potpitanja_1==1)document.getElementById('oduzmidugme1').style.display='none';
        poslenje_potp=document.getElementsByClassName('potpitanje_1');
        poslenje_potp[br_potpitanja_1].remove();
    }

    function dodaj_izbor1(){
        var potp=document.getElementsByClassName('izbor_1');
        // console.log(potp);
        var htmltext='<div class=\"field izbor_1\" >\n'+
            '<label>Izbor:</label>\n'  +
            '<input type=\"text\" name=\"izbor[]\" class=\"izbValue\" placeholder=\"\">\n' +
            '</div>';
        // console.log(potp);
        potp=potp[br_izbora-1];
        potp.insertAdjacentHTML('afterend', htmltext);
        br_izbora++;
        if(br_izbora>1){document.getElementById('oduzmidugme2').style.display='inline';}


    }
    function oduzmi_izbor1 (){
        if(br_izbora==1)return;
        br_izbora--;
        if(br_izbora==1)document.getElementById('oduzmidugme2').style.display='none';
        poslednji_izb=document.getElementsByClassName('izbor_1');
        poslednji_izb[br_izbora].remove();
    }

    $(document).ready(function(){
        $('#tipp').change(function(){
            //Selected value
            var tip = $(this).val();

            dodaj_pitanje(tip);

        });
    });

    var p=[];
    var pp_uk=[];
    var izb_uk=[];
    var tip_uk=[];
    var ob_uk=[];
    var jj=0;
    var naz;
    var dat_pocetka;
    var dat_kraja;
    var anon;
    var brstr;

    function nastavi(){

        naz=document.naform.anknaziv.value;
        dat_pocetka=document.naform.datpoc.value;
        dat_kraja=document.naform.datkr.value;
        anon=document.naform.an.value;

        // var a=document.naform.potp;
        var tp=document.getElementById('tipp').value;
        ii=ii+1;
        if(tp>'0') {
            tip_uk[ii - 1] = tp;
        }
        if(naz=='' || dat_pocetka=='' || dat_kraja=='' || anon=='')return;
if(ii>=2){brstr=document.getElementById('brstr').value;}
        if(ii>=1) {

            var pitanje = document.naform.pit1.value;
            var ob = document.naform.ob1.value;
            if(pitanje=='')return;
            var j = 0;
            var potp = [];
            var potpitanja = document.getElementsByClassName('potpValue');
            console.log(potpitanja);
            while (pp = potpitanja[j]) {
                potp[j] = pp.value;
                if(potp[j]=='')return;
                j++;
            }
            p[ii - 1] = pitanje;
            ob_uk[ii-1]=ob;
            pp_uk[ii - 1] = potp;


            if (tp === '6' || tp === '7') {
                k = 0;
                var izb = [];
                var izbor1 = document.getElementsByClassName('izbValue');
                while (iz = izbor1[k]) {
                    izb[k] = iz.value;
                    if(izb[k]=='')return;
                    k++;
                }
                izb_uk[jj] = izb;
                jj++;
            }
            document.getElementById('pit1div').innerHTML = '<div class="field">\n' +
                '\n' +
                '<label>Pitanje:</label>\n' +
                '<input type="text" name="pit1" id="pit1" placeholder="" required>\n' +
                '</div>\n' +
                '<div class="field potpitanje_1">\n' +
                '<label>Potpitanje:</label>\n' +
                '<input type="text" name="potp[]" class="potpValue" placeholder="" required>\n' +
                '</div>';

            document.getElementById('oduzmidugme1').style.display='none';
            document.getElementById('divizbor').innerHTML = '<div class="field izbor_1">\n' +
                '<label>Izbor:</label>\n' +
                ' <input type="text" name="izbor[]" class="izbValue"  placeholder="" >' +
                '</div>\n'+
                '<button class="ui button " onclick="dodaj_izbor1()" type="button" >Dodaj</button>\n' +
                '<button class="ui button hidden" style="display:none;" id="oduzmidugme2" onclick="oduzmi_izbor1()" type="button" >Oduzmi</button>';
            document.getElementById('brstran').innerHTML ='<br><div class="field str">\n'+
                '<label>Broj stranica:</label>\n' +
                ' <input type="number" name="brstr" id="brstr" min="1" max="*jsvariable*" class=""  placeholder="" >' +
                '</div>\n';
            var input = document.getElementById("brstr");
            input.setAttribute("max",ii+1);

        }

            document.getElementById('tipp').value='-1';
            console.log(p);
            console.log(pp_uk);
            console.log(izb_uk);
            console.log(tip_uk);
            br_potpitanja_1=1;
            br_izbora=1;

        document.getElementById('naform').style.display='none';
        document.getElementById('brstran').style.display='none';
        document.getElementById('tippit').style.display='block';
        document.getElementById('divdugme').style.display='none';
        document.getElementById('ankdiv').style.display = 'none';
        document.getElementById('nadugme').style.display = 'none';
        document.getElementById('divizbor').style.display='none';
    }

  function nova_anketa(){
        nastavi();
        if(brstr=='' || brstr==null)brstr=1;
            if(p.length<brstr){alert('Preveliki broj strana!');}
            p=JSON.stringify(p);
            pp_uk=JSON.stringify(pp_uk);
            izb_uk=JSON.stringify(izb_uk);
            tip_uk=JSON.stringify(tip_uk);
            // ob_uk=JSON.stringify(ob_uk);
      // ob_uk=[];
      ob_uk=JSON.stringify(ob_uk);

            var link='inc/submit_anketa.php?p='+p+'&pp_uk='+pp_uk+'&izb_uk='+izb_uk+'&tip_uk='+tip_uk+'&ob_uk='+ob_uk+'&naz='+naz+'&dat_pocetka='+dat_pocetka+'&dat_kraja='+dat_kraja+'&anon='+anon+'&brstr='+brstr;
            console.log(link);
            window.location.href=link;
            // $.get('inc/submit_anketa.php', { p: p , pp_uk: pp_uk, izb_uk: izb_uk, tip_uk: tip_uk, ob_uk: ob_uk, naz:naz, dat_pocetka:dat_pocetka, dat_kraja:dat_kraja, anon:anon  }, function(data){
            //     // console.log(data);
            // });
        }



    function dodaj_pitanje(tip){
        if(tip!=='6' && tip!=='7') {
            document.getElementById('tippit').style.display='block';
            document.getElementById('naform').style.display='block';
            document.getElementById('pitanje1').style.display = 'block';
            document.getElementById('brstran').style.display='block';
            document.getElementById('ankdiv').style.display = 'none';
            document.getElementById('nadugme').style.display = 'block';
            document.getElementById('divdugme').style.display='block';
            document.getElementById('divizbor').style.display='none';
        }
        if(tip==='6' || tip==='7'){
            document.getElementById('tippit').style.display='block';
            document.getElementById('naform').style.display='block';
            document.getElementById('pitanje1').style.display = 'block';
            document.getElementById('brstran').style.display='block';
            document.getElementById('ankdiv').style.display = 'none';
            document.getElementById('nadugme').style.display = 'block';
            document.getElementById('divdugme').style.display='block';
            document.getElementById('divizbor').style.display='block';
        }
       }
    $('.linkdugme').click(function(){
        var ida=$(this).attr('ida');
        // console.log(ida);
        // $.get('pretragaAnketa.php', { ida: ida}, function(data){
        //     console.log(data);
        // });
        window.location.href='pretragaAnketa.php?ida='+ida+'&pop=1';
    });
    $('.brisidugme').click(function(){
        var ida=$(this).val();
        $(this).parent().parent().remove();
        // console.log(ida);
        $.get('brisiAnketa.php', { ida: ida}, function(data){
            // console.log(data);
        });
    });
    $('.izmenidugme').click(function(){
        var ida=$(this).attr('ida');
        // alert(1);
        // console.log(ida);

        $.get('izmeniAnketa.php', { ida: ida}, function(data){
            // alert(2);
            // console.log(data);
            data=JSON.parse(data);
            // alert(111);
            // console.log(data);
            izlistaj_pitanja(data);
        });
    });




    function izlistaj_pitanja(data){
        document.getElementById('promenaTab').innerHTML='';
        var i=0;
        table='';
        table+="<thead>\n";
        table+="<tr>\n";
        table+="<th>Pitanje</th>\n";
        table+="<th>Tip pitanja</th>\n";
        table+="<th></th>\n";
        table+="</thead>\n";
        table+="<tbody>\n";

        while(niz=data[i]){
            var row = "";
            row = napraviRedpit(niz);
            table += row;
            i++;
        }
        table+="</tbody>\n";
        table+="</table>\n";
        document.getElementById('promenaTab').innerHTML=table;
    }
    function napraviRedpit(niz){
        var row = "";
        row += "<tr>\n";
        row+="<td><a class='izmpitdugme' href='#' idp='"+niz['idPitanja']+"' onclick='izmenipit(this)'>"+niz['tekst']+"</a></td>";
        row+="<td>"+niz['tipOdgovora']+"</td>";
        row+="<td><button class='ui button'  value='"+niz['idPitanja']+"' onclick='promeni_pitanje(this)'>IZMENI</button>" +
            "&nbsp;<button class='ui button '  value='"+niz['idPitanja']+"' onclick='izbrisi_pit("+niz['idPitanja']+")'>IZBRISI</button>" +
            "&nbsp;<button class='ui button '  value='"+niz['idPitanja']+"' onclick='redosled_potp(this.value)'>PROMENA REDOSLEDA POTPITANJA</button></td>";
        row += "</tr>\n";
        return row;

    }
    function izlistaj_potpitanja(data){
        document.getElementById('promenaTab').innerHTML='';
        var i=0;
        table='';
        table+="<thead>\n";
        table+="<tr>\n";
            table+="<th>Potpitanje</th>\n";
        // table+="<th>Tip pitanja</th>\n";
        table+="<th></th>\n";
        table+="</thead>\n";
        table+="<tbody>\n";

        while(niz=data[i]){
            var row = "";
            row = napraviRedpotp(niz);
            table += row;
            i++;
        }
        table+="</tbody>\n";
        table+="</table>\n";
        document.getElementById('promenaTab').innerHTML=table;
    }

    function napraviRedpotp(niz){
        var row = "";
        row += "<tr>\n";
        // row+="<td><a class='izmpotpdugme' href='#' idpotp='"+niz['idPotpitanja']+"'>"+niz['potpitanje']+"</a></td>";
        row+="<td>"+niz['potpitanje']+"</td>";
        // row+="<td>"+niz['tipOdgovora']+"</td>";
        row+="<td><button class='ui button  '  value='"+niz['idPotpitanja']+"' onclick='izm_potp("+niz['idPotpitanja']+")'>IZMENI</button>" +
            "&nbsp;<button class='ui button '  value='"+niz['idPotpitanja']+"' onclick='izbrisi_potp("+niz['idPotpitanja']+")' >IZBRISI</button></td>";
        row += "</tr>\n";
        return row;

    }
    function izmenipit(e){
        // alert('aaa');
        var idp=e.getAttribute('idp');
        console.log(idp);

        $.get('izmeniPitanje.php', { idp: idp}, function(data){
            // alert(2);
            console.log(data);
            data=JSON.parse(data);
            // alert(111);
            // console.log(data);
            izlistaj_potpitanja(data);
        });
    }

    function promeni_pitanje(e){
        var idp=e.value;
        window.location.href='izmeniPit.php?idp='+idp;
    }
    function izm_potp(idpotp){
        // alert(1);
        // alert(idpotp);
        window.location.href='izmeniPotp.php?idpotp='+idpotp;
    }
    function izbrisi_potp(idpotp){
        window.location.href='izbrisiPotp.php?idpotp='+idpotp;
    }
    function izbrisi_pit(idpit){
        window.location.href='izbrisiPit.php?idpit='+idpit;
    }

    // $(document).ready(function(){
    //     $('#ankselect').change(function(){
    //         //Selected value
    //         var inputValue = $(this).val();
    //         var inputValue1 = document.getElementById('pitselect').value;
    //         //alert("value in js "+inputValue);
    //         //console.log(inputValue);
    //         //console.log(inputValue1);
    //         //Ajax for calling php function
    //         $.get('ubaci_postojece_pitanje.php', { inputValue: inputValue, inputValue1: inputValue1 }, function(data){
    //             // data=JSON.parse(data);
    //             // console.log(data);
    //
    //             // alert('ajax completed. Response:  '+data);
    //             //do after submission operation in DOM
    //         });
    //     });
    // });
    function ubaci(){
            //Selected value
            var inputValue = document.getElementById('ankselect').value;
            var inputValue1 = document.getElementById('pitselect').value;
        //     alert(inputValue);
        // alert(inputValue1);
            //alert("value in js "+inputValue1);
            //console.log(inputValue);
            //console.log(inputValue1);
            //Ajax for calling php function
            $.get('ubaci_postojece_pitanje.php', { ida: inputValue, idp: inputValue1 }, function(data){
                alert("Uspesno ubaceno pitanje");
                //alert('ajax completed. Response:  '+data);
                // data=JSON.parse(data);
                // console.log(data);
                //do after submission operation in DOM
            });
    }
    function redosled(ida){
        window.location.href='promeni_redosled.php?ida='+ida;
    }
    function redosled_potp(idp){
        window.location.href='promeni_redosled_potp.php?idp='+idp;
    }




</script>

