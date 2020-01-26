<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin verification</title>

    <style>
        .btn {
            background-color: #ccc;
            border: 1px solid #ccc;
            padding: 4px 8px;
            text-align: center;
            color: #000;
            background: #2185d0;
            cursor: pointer;
            display: inline-block;
            min-height: 1em;
            outline: 0;
            vertical-align: baseline;
            text-transform: none;
            text-shadow: none;
            font-weight: 400;
            font-style: normal;
            text-align: center;
            text-decoration: none;
            border-radius: 3px;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }
    </style>
</head>

<body>
<p>
<center>
    <img src="cid:logo.png" style="display: block;"/>
    Welcome, <b>${username}</b>! <br/>
    Glad to have you on board.
</center>
<br/>

Please confirm your account by clicking the button below:
<p align="center">
    <#assign verificationURL>${CONFIRMATION_URL}</#assign>
    <a href="${verificationURL}" role="button" class="btn">Confirm</a>
</p>

<#--<table>-->
<#--    <tr>-->
<#--        <#assign verificationURL>${CONFIRMATION_URL}</#assign>-->
<#--        Once confirmed, you'll be able to login to-->
<#--        <a href="${verificationURL}" rel="link">${verificationURL}</a> <br/>-->
<#--        with your new account. <br/>-->
<#--    </tr>-->
<#--</table>-->

Best wishes, <br/>
Cogent Team
</body>
</html>
