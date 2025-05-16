function validation(event){
    let mail = document.getElementById("mail").value;
    let password = document.getElementById("password").value;
    if(mail.length > 0 && password.length > 0){
        if(validationMail(mail) && validationPassword(password)){
            console.log("job");
        }else{
            console.log("job no");
            event.preventDefault();
        }
    }else{
        console("empty fields");
    }
}
function validationMail(mail){
    let validation = true;
    mail = normalizeData(mail);
    if((mail.substring(0,mail.indexOf("@"))).length < 1
    || (mail.substring(mail.indexOf("@")+1,mail.lastIndexOf("."))).length < 1
    || (mail.substring(mail.indexOf(".")+1)).length < 2){
        console.log("error in mail");
        alert("error in mail");
        return !validation;
    }
    console.log(validation);
    return validation;
}
function validationPassword(password){
    let validation = true;
    password = normalizeData(password);
    if(password.length < 6 ||!/[A-Z]/.test(password)
    ||!/[\#\%\&\$]/.test(password)||!/[0-9]/.test(password)){
        console.log("error in password");
        alert("error in password");
        return !validation;
    }
    console.log(validation);
    return validation;
}
function normalizeData(string){
    string = string.trim();
    return string
}