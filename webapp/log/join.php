<?php
require '../php/log.php';
$name = $_POST['name'];
$surname = $_POST['surname'];
$mail = $_POST['mail'];
$password = $_POST['password'];

$rest = $con -> query("insert into users(name,surname,mail,password) values ('$name','$surname','$mail','$password')");
if($rest){
    $con->close();
    header('Location: ../html/login.html');
    exit;
}else{
    echo("no");
}

$con->close();
?>