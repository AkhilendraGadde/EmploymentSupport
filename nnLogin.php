<?php
    $con = mysqli_connect("localhost", "id609849_reglogindb", "Gaddeess", "id609849_testlogin");
	
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT name,username,password,email,phone,type FROM user WHERE username = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $name, $username, $password, $email, $phone, $type);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["name"] = $name;
        $response["email"] = $email;
        $response["username"] = $username;
        $response["password"] = $password;
		$response["phone"] = $phone;
		$response["type"] = $type;
    }
    
    echo json_encode($response);
?>
