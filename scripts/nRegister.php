<?php
    $con = mysqli_connect("localhost", "id609849_reglogindb", "Gaddeess", "id609849_testlogin");
    
    $name = $_POST["name"];
    $username = $_POST["username"];
    $password = $_POST["password"];
	$email = $_POST["email"];
$gender= $_POST["gender"];
	$phone = $_POST["phone"];
	$dob = $_POST["dob"]; 
	$location = $_POST["location"];
	$type = $_POST["type"];
	$type_id = $_POST["type_id"];
$otp = $_POST["otp"];
$otp_verified = $_POST["otp_verified"];

//$otp = 'xxxxx';
//$otp_verified = 0;
    
function registerUser() {
        global $con, $name, $username, $password, $email, $gender, $phone, $dob, $location, $type, $type_id, $otp, $otp_verified;
        $statement = mysqli_prepare($con, "INSERT INTO user (name, username, password, email, gender, phone, location, dob, type, type_id, otp, otp_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        mysqli_stmt_bind_param($statement, "sssssisssisi", $name, $username, $password, $email, $gender, $phone, $location, $dob, $type, $type_id, $otp, $otp_verified);
        mysqli_stmt_execute($statement);
        mysqli_stmt_close($statement);     
    }

    function usernameAvailable() {
        global $con, $username;
        $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ?"); 
        mysqli_stmt_bind_param($statement, "s", $username);
        mysqli_stmt_execute($statement);
        mysqli_stmt_store_result($statement);
        $count = mysqli_stmt_num_rows($statement);
        mysqli_stmt_close($statement); 
        if ($count < 1){
            return true; 
        }else {
            return false; 
        }
    }
	
	function emailAvailable() {
        global $con, $email;
        $statement = mysqli_prepare($con, "SELECT * FROM user WHERE email = ?"); 
        mysqli_stmt_bind_param($statement, "s", $email);
        mysqli_stmt_execute($statement);
        mysqli_stmt_store_result($statement);
        $count = mysqli_stmt_num_rows($statement);
        mysqli_stmt_close($statement); 
        if ($count < 1){
            return true; 
        }else {
            return false; 
        }
    }
	
	function phoneAvailable() {
        global $con, $phone;
        $statement = mysqli_prepare($con, "SELECT * FROM user WHERE phone = ?"); 
        mysqli_stmt_bind_param($statement, "i", $phone);
        mysqli_stmt_execute($statement);
        mysqli_stmt_store_result($statement);
        $count = mysqli_stmt_num_rows($statement);
        mysqli_stmt_close($statement); 
        if ($count < 1){
            return true; 
        }else {
            return false; 
        }
    }

    $response = array();
    $response["success"] = false;  

    if (usernameAvailable())
   {
	if(emailAvailable())
	{
			if(phoneAvailable())
                       {
				registerUser();
				$response["success"] = true; 
			} 
	}  
    }


    echo json_encode($response);
?>