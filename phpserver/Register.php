<?php 
    $con = mysqli_connect("localhost", "psjoo7pproject", "xorhdakd!2", "psjoo7pproject");
    mysqli_query($con,'SET NAMES utf8');

    $UserID = isset($_POST["UserID"]) ? $_POST["UserID"] : "";
    $UserPwd = isset($_POST["UserPwd"]) ? $_POST["UserPwd"] : "";
    $UserName = isset($_POST["UserName"]) ? $_POST["UserName"] : "";
    $UserPhone = isset($_POST["UserPhone"]) ? $_POST["UserPhone"] : "";
    $UserAdd = isset($_POST["UserAdd"]) ? $_POST["UserAdd"] : "";

    $statement = mysqli_prepare($con, "INSERT INTO USERLIST VALUES (?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssis", $UserID, $UserPwd, $UserName, $UserPhone, $UserAdd);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;
 
   
    echo json_encode($response);


?>