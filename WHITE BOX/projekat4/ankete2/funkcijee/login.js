//document.writeln('<script src="/javascripts/jquery.js" type="text/javascript"></script>');

function showdiv(a, b, c) {
document.getElementById(a).style.display = 'block';
document.getElementById(b).style.display = 'none';
document.getElementById(c).style.display = 'none';
}


function proveraLogin() {
    var greska = '';
    var ok = true;
    if (document.formlogin.username1.value === '')
    {
        greska += 'Korisničko ime ne sme biti prazno!<br/>';
        ok = false;
    }
    if (document.formlogin.password1.value === '')
    {
        greska += 'Lozinka ne sme biti prazna!<br/>';
        ok = false;
    }
    // if (ok) {
    //     return true;
    // } else {
    //     return false;
    // }
    if(!ok)
    alert(greska);
    return ok;

}

function proveraNovaloz(){
    var greska = '';
    var ok = true;
    if (document.formnew.username3.value === ''){
        greska += 'Korisničko ime ne sme biti prazno!<br/>';
        ok = false;
    }
    if (document.formnew.password3.value === ''){
        greska += 'Stara lozinka ne sme biti prazna!<br/>';
        ok = false;
    }
    if (document.formnew.newpass.value === ''){
        greska += 'Nova lozinka ne sme biti prazna!<br/>';
        ok = false;
    }
    if (document.formnew.newpass1.value === ''){
        greska += 'Morate ponoviti novu lozinku!<br/>';
        ok = false;
    }
    if (document.formnew.newpass.value !== document.formnew.newpass1.value){
        greska += 'Lozinke se moraju slagati!<br/>';
        ok = false;
    }

    if (ok) {
        if (!proveraSifra(document.formnew.newpass.value)) {
            greska += 'Lozinka ne zadovoljava kriterijume tezine!<br/>';
            ok = false;
        }
    }
    if (ok) {
        return true;
    } else {
        alert(greska);
        //prikaziGresku(greska);
        return false;
    }
}



 function proveraReg() {
    //alert(1);
  var greska = '';
  var ok = true;
  var dd = document.formreg.datum.value;
  //var tip = document.formreg.getElementsByName("tipkor").value;
  //if(tip==="ispitanik"){
     //alert(1);
  if (document.formreg.username2.value === '') {
   greska += 'Korisničko ime ne sme biti prazno!<br/>';
   ok = false;
  }
  if (document.formreg.password2.value === '') {
   greska += 'Lozinka ne sme biti prazna!<br/>';
   ok = false;
  }
  if (document.formreg.password2.value !== document.formreg.password22.value) {
   greska += 'Lozinke se moraju slagati!<br/>';
   ok = false;
  }
  if (!proveraUsername(document.formreg.username2.value)) {
      //alert(1);
   greska += 'Korisničko ime nije ispravno uneto!<br/>';
   ok = false;
  }


  //if (ok) {
   if (!proveraSifra(document.formreg.password2.value)) {
        greska += 'Lozinka ne zadovoljava kriterijume tezine!<br/>';
        ok = false;
   }
 // }

  if (!proveraIme(document.formreg.ime.value)) {
      alert(1);
      greska += 'Ime nije ispravno uneto!<br/>';

      ok = false;
  }

  if (!proveraIme(document.formreg.prezime.value)) {
      greska += 'Prezime nije ispravno uneto!<br/>';

      ok = false;
  }

  if (!proveraJMBG(document.formreg.jmbg.value,dd)) {
      greska += 'JMBG nije ispravno unet!<br/>';
      alert(greska);
      ok = false;
  }
    alert(222);
  if (!proveraMejl(document.formreg.mejl.value)) {
      greska += 'Mejl nije ispravno unet!<br/>';
      ok = false;
  }

  if (!proveraTel(document.formreg.telefon.value)) {
      greska += 'Telefon nije ispravno unet!<br/>';
      ok = false;
  }

  //alert(ok);
  //alert(1111);
  if (ok) {
      //alert(1);
  } else {
      alert(greska);
  }
  return ok;
 }
 //}
 //else if(tip==="Sluzbenik"){
 // var greska = '';
 // var ok = true;
 // if (document.formreg.username3.value === '')
 // {
 // greska += 'Korisničko ime ne sme biti prazno!<br/>';
 // ok = false;
 // }
 // if (document.formreg.password3.value === '')
 // {
 // greska += 'Lozinka ne sme biti prazna!<br/>';
 // ok = false;
 // }
 // if (document.formreg.password3.value !== document.formreg.passwword33.value)
 // {
 // greska += 'Lozinke se moraju slagati!<br/>';
 // ok = false;
 // }
 // if (ok) {
 // if (!proveraUsername(document.formreg.username3.value))
 // {
 // greska += 'Korisničko ime nije ispravno uneto!<br/>';
 // ok = false;
 // }
 // }
 //
 // if (ok) {
 // if (!proveraSifra(document.formreg.password3.value)) {
 // greska += 'Lozinka ne zadovoljava kriterijume tezine!<br/>';
 // ok = false;
 // }
 // }
 //
 // if (!proveraIme(document.formreg.imeKomp.value))
 // {
 // greska += 'Naziv nije ispravno unet!<br/>';
 // ok = false;
 // }
 // if (!proveraIme(document.formreg.grad.value))
 // {
 // greska += 'Grad nije ispravno unet!<br/>';
 // ok = false;
 // }
 //
 // if (!proveraAdresa(document.formreg.adresa.value))
 // {
 // greska += 'Adresa nije ispravno uneta!<br/>';
 // ok = false;
 // }
 // if (!proveraIme(document.formreg.direktor.value))
 // {
 // greska += 'Direktor nije ispravno unet!<br/>';
 // ok = false;
 // }
 //
 // // if (!proveraPIB(document.formreg.pib.value))
 // // {
 // // greska += 'PIB nije ispravno unet!<br/>';
 // // ok = false;
 // // }
 // // if (!proveraWWW(document.formreg.sajt.value))
 // // {
 // // greska += 'WEB stranica nije ispravno uneta!<br/>';
 // // ok = false;
 // // }
 //
 // // if (!slikaProveraExt(document.formreg.logoKompanija.value))
 // // {
 // // greska += 'Slika nije u ispravnom formatu!<br/>';
 // // ok = false;
 // // }
 //
 // if (isNaN(document.formreg.regKompanijaBrZap.value))
 // {
 // greska += 'Broj zaposlenih nije u ispravnom formatu!<br/>';
 // ok = false;
 // }
 //
 // if (ok) {
 // return true;
 // } else {
 // prikaziGresku(greska);
 // return false;
 // }
 //}
 
 
 
 function proveraUsername(user) {
    //alert(1);
   r = /^([A-Z]|[a-z]|[0-9]){1,20}$/;
   return r.test(user);
 }
 //r = /^[a-z]{1}([-\_.A-Za-z0-9]){1,20}$/;
 //  r = /^([A-Z]|[a-z]|(0-9)){1,20}$/;
 // if (!r.test(user))
 // return false;
 // return true;

 
 function proveraSifra(sifra) {
     //r = /^([A-Z]|[a-z]){1}([#*.!?$]|[A-Z]|[a-z]|[0-9]){7,11}$/;
     r = /^([A-Z]|[a-z]|[0-9]|[#*.!?$]){5,15}$/;
     return r.test(sifra);
 }
 // sr = /\d/g;
 // if (!r.test(sifra))
 // return false;
 // r = /[A-Z]/g;
 // if (!r.test(sifra))
 // return false;
 // sifratest = /[a-z]/g;
 // if (sifra.match(r).length < 3)
 // return false;
 // sifratest = /[#*.!?$]/g;
 // if (!r.test(sifra))
 // return false;
 //
 // for (i = 0; i < sifra.length - 1; i++) {
 // if (sifra[i] == sifra[i + 1])
 // return false;
 // }
 // return true;
 // }
 
function proveraIme(ime) {
   r = /^[A-Z][a-z]+(\s[A-Z][a-z]+)?$/;
   return r.test(ime);
 // if (!r.test(ime))
 // return false;
 // return true;
 }

function proveraJMBG(jmbg,d){

    //alert($('#datum').val());
    //var d = document.formreg.datum.value;
    alert(111);
    var date = new Date(document.formreg.datum.value);
    console.log(date);
  //var date = new Date(Date.parse($('#datum').val()));

  day = date.getDate();

  month = date.getMonth() + 1;

  year = date.getFullYear();

  if(jmbg.length!==13){

   return false;
  }
  if(day!==parseInt(jmbg.substring(0,2))||month!==parseInt(jmbg.substring(2,4))||(year.toString()).substring(1,4)!==jmbg.substring(4,7)){
      return false;
  }
  r=/^[0-9]{5}$/;
  if(!r.test(jmbg.substring(7,12))){return false;}
  kb = mod11(jmbg.substring(0, 12));
  if(kb!==parseInt(jmbg.charAt(12), 10)||kb===1){
      alert("LOSE");
   return false;
  }
  return true;

}
//  r = /^[A-Z][a-z]+(\s[A-Z][a-z]+)?$/;
//  return r.test();
// }

function mod11(br) {
 var kb = 0;
 for (var i = br.length - 1, mnozilac = 2; i >= 0; i--) {
  kb += parseInt(br.charAt(i), 10) * mnozilac;
  mnozilac = mnozilac === 7 ? 2 : mnozilac + 1;
 }
 k = kb % 11;
 if(k===0){kb = 0;}
 if(k>1){kb = 11-k;}
 //if(k===1){return false;}
 //kb = 11 - (kb % 11);
 return kb;
}

 function proveraMejl(mejl) {
 //r = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  r = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  return r.test(String(mejl).toLowerCase());
 //  if (!(r.test(String(mejl).toLowerCase())))
 // return false;
 // return true;
 }
 
 function proveraTel(tel) {
     r = /^[+]381[0-9]{8,9}$/;
     return r.test(tel);
 }
 // if (!r.test(tel))
 // return false;
 // return true;
 // }
 
 // function slikaProveraExt(slika) {
 // if (slika == '')
 // return false;
 // var x = slika.split('.');
 // var ext = x[x.length - 1];
 // switch (ext.toLowerCase()) {
 // case 'jpg':
 // case 'png':
 // break;
 // default:
 // return false;
 // }
 // return true;
 //
 // }
 // }*/



















