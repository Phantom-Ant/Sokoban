<?php
$con = mysqli_connect("localhost","root","root","sokoban");
if(!$con){
    trigger_error(mysqli_connect_error());
}
?>