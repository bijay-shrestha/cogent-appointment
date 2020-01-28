<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Updated Admin Information</title>
</head>
<body>

<center>
    <img src="cid:logo.png" style="display: block;"/>
    Hello, <b>${username}</b>! <br/>
</center>
<br/>

Following details has been updated :
<ul>
    <#list updatedData as data>
        <li> ${data}</li>
    </#list>
</ul>

<ul>
    <#assign hasMacBinding>${hasMacBinding}</#assign>
    <#if hasMacBinding=='Y'>
        Mac Address(s): <br/>
        <#list updatedMacAddress as macAddress>
            ${macAddress_index + 1}. ${macAddress}<#if macAddress_has_next><br/></#if>
        </#list>
    <#else>
        Mac Address(s):
        <#list updatedMacAddress as macAddress>
            ${macAddress}
        </#list>
    </#if>
</ul>

Thank you. <br/>
Best wishes, <br/>
Cogent Team
</body>
</html>
