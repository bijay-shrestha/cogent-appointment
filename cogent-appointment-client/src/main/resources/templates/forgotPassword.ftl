<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login Password Reset</title>
</head>
<body>
<p>
<center>
    <img src="cid:logo.png" style="display: block;"/>
    Hi <b>${username}</b>,<br/>
   We received a request to reset your password. Enter the following password reset code: <br/>

    <table cellspacing="0" cellpadding="0" align="center" style="font-size: 21px; padding: 15px;
     background: #fffccd; border-width: 1px; border-color: #c1bf9b; border-style: solid;
     border-radius: 10px; font-size: 50px;" bgcolor="#fffccd">
        <tr>
            <td width="200px">
                <b>${resetCode}</b>
            </td>
        </tr>
    </table>
</center>
<br/>

Best wishes, <br/>
Cogent Team
</body>
</html>
