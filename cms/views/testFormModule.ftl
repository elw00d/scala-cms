<#if entered??>You entered: ${entered}</#if>
<form action="${baseModuleUrl}/do_something" method="post">
    <input type="text" name="name"/>
    <input type="submit" value="Submit"/>
</form>