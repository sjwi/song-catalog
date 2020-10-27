function unlockForm(form, passwords){
	var validPswds = false;
	if (passwords){
		validPswds = arePasswordsValid(form);
	}
	if (isFormFilledSilent(form) && validPswds){
		$(form).find(':submit').prop('disabled',false);
	} else {
		$(form).find(':submit').prop('disabled',true);
	}
}
function arePasswordsValid(form){
	if ($(form).find('.password').val() != "" && $(form).find('.confirm-password').val() != ""){
		if ($(form).find('.password').val() != $(form).find('.confirm-password').val()){
			$(form).find('.password, .confirm-password').css({'border':'1px solid','border-color':'red'})
			return false;
		} else {
			$(form).find('.password, .confirm-password').css({'border':'1px solid','border-color':'green'})
			return true;
		}
	}
}
function isPasswordPolicyCompliant(pwd){
	if (pwd.length < 4){
		return false;
	} else {
		return true;
	}
}
function isFormFilledSilent(form){
	var returnVal = true;
	$(form).find('input.required:visible').each(function(){
        if( $(this).val() == "" ){
        	returnVal = false;
        }
    });
	return returnVal;
}

function isFormFilled(form){
	var returnVal = true;
	$(form).find(':input.required:visible').each(function(){
        if( $(this).val() == "" ){
        	alertWithFade('danger', $(this).attr('title'));
        	$(this).addClass('invalid-form-input');
        	returnVal = false;
        	return false;
        }
        $(this).val(cleanInput($(this).val()));
        $(this).removeClass('invalid-form-input');
    });
	return returnVal;
}

function cleanInput(input){
	 if(input == undefined){
		 input = '';
	 }
	 if (typeof input == "string"){
		 return input.replace(/[\u2018\u2019]/g, "'").replace(/[\u201C\u201D]/g, '"').replace("''","'");
	 }
	 else return input;
}
