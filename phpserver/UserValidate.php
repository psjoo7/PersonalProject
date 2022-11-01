<?php
    $con = mysqli_connect("localhost", "psjoo7pproject", "xorhdakd!2", "psjoo7pproject");
    mysqli_query($con,'SET NAMES utf8');

    $UserID = isset($_POST["UserID"]) ? $_POST["UserID"] : "";

    $statement = mysqli_prepare($con, "SELECT UserID FROM USERLIST WHERE UserID = ?");

    mysqli_stmt_bind_param($statement, "s", $UserID);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $UserID);

    $response = array();
    $response["success"] = true;

    while(mysqli_stmt_fetch($statement)){
      $response["success"] = false;
      $response["UserID"] = $UserID;
    }

    echo json_encode($response);
?>