// Übersetze Validationsmeldungen
jQuery.extend(jQuery.validator.messages, {
    required: texts['javascript_validation_required'],
    remote: texts['javascript_validation_remote'],
    email: texts['javascript_validation_email'],
    url: texts['javascript_validation_url'],
    date: texts['javascript_validation_date'],
    dateISO: texts['javascript_validation_dateISO'],
    number: texts['javascript_validation_number'],
    digits: texts['javascript_validation_digits'],
    creditcard: texts['javascript_validation_creditcard'],
    equalTo: texts['javascript_validation_equalTo'],
    accept: texts['javascript_validation_accept'],
    maxlength: jQuery.validator.format(texts['javascript_validation_maxlength']),
    minlength: jQuery.validator.format(texts['javascript_validation_minlength']),
    rangelength: jQuery.validator.format(texts['javascript_validation_rangelength']),
    range: jQuery.validator.format(texts['javascript_validation_range']),
    max: jQuery.validator.format(texts['javascript_validation_max']),
    min: jQuery.validator.format(texts['javascript_validation_min'])
});

$(document).ready(function() {

    /* ****************************************** */
    /* Form Wizard */
    /* ****************************************** */
    $(".wizard").each(function() {
        let wizForm = $(this);
        let jsfForm = wizForm.closest('form');
        let jsfFormName = jsfForm.attr('name');
        
        let stepsOptions = {
            headerTag: "h2",
            transitionEffect: "slide",
            autoFocus: true,
            onFinished: function (event, currentIndex) { $("#" + jsfFormName + '\\:hidden_jsf_submit').trigger('click') },
            labels: {
                cancel: texts["javascript_wizard_button_cancel"],
                current: texts["javascript_wizard_button_current_step"],
                pagination: texts["javascript_wizard_button_pagination"],
                finish: texts["javascript_wizard_button_finish"],
                next: texts["javascript_wizard_button_next"],
                previous: texts["javascript_wizard_button_previous"],
                loading: texts["javascript_wizard_loading"],
            }
        };

        // Spezialfall "Mandant erfassen"
        if(wizForm.is('#manageClientForm')) {
            stepsOptions.onStepChanging = function (event, currentIndex, newIndex) {

                // Erlaube zurück auch wenn Schritt nicht validiert
                if (currentIndex > newIndex)
                {
                    return true;
                }

                
                if (newIndex === 1 && $("#" + jsfFormName + "\\:client_name").hasClass("error"))
                {
                    return false;
                }
                // Needed in some cases if the user went back (clean up)
                if (currentIndex < newIndex)
                {
                    // To remove error styles
                    wizForm.find(".body:eq(" + newIndex + ") label.error").remove();
                    wizForm.find(".body:eq(" + newIndex + ") .error").removeClass("error");
                }
                jsfForm.validate().settings.ignore = ":disabled,:hidden";
                return jsfForm.valid();
                //return true;
            };
            
        }

        wizForm.steps(stepsOptions);

        jsfForm.validate({
            errorPlacement: function errorPlacement(error, element) { element.after(error); }
        });
    });



    /* Sidebar navigation */
    /* ------------------ */
    $(window).resize(function() {
        if ($(window).width() >= 767) {
            $(".side-nav").slideDown(150);
        } else {
            $(".side-nav").slideUp(150);
        }
    });



    /* ****************************************** */
    /* Sidebar dropdown */
    /* ****************************************** */
    $(".sidebar-dropdown a").on('click', function(e) {
        e.preventDefault();

        if (!$(this).hasClass("open")) {
            // hide any open menus and remove all other classes
            $(".sidebar .side-nav").slideUp(150);
            $(".sidebar-dropdown a").removeClass("open");

            // open our new menu and add the open class
            $(".sidebar .side-nav").slideDown(150);
            $(this).addClass("open");
        } else if ($(this).hasClass("open")) {
            $(this).removeClass("open");
            $(".sidebar .side-nav").slideUp(150);
        }
    });


    /* ****************************************** */
    /* Sidebar dropdown menu */
    /* ****************************************** */
    $(".has_submenu > a").click(function(e) {
        e.preventDefault();
        var menu_li = $(this).parent("li");
        var menu_ul = $(this).next("ul");

    if(menu_li.hasClass("open")){
      menu_ul.slideUp(150);
      menu_li.removeClass("open");
      $(this).find("span").removeClass("fa-caret-up").addClass("fa-caret-down");
    } else {
      $(".side-nav > li > ul").slideUp(150);
      $(".side-nav > li").removeClass("open");
      menu_ul.slideDown(150);
      menu_li.addClass("open");
      $(this).find("span").removeClass("fa-caret-down").addClass("fa-caret-up");
    }
  });
  

    
    /* ****************************************** */
    /* Slim Scroll */
    /* ****************************************** */
    $('.scroll').slimScroll({
        height: '315px',
        size: '5px',
        color:'rgba(50,50,50,0.3)'
    });


    /* ****************************************** */
    /* Knob */
    /* ****************************************** */
    /*
     $(function() {
     $(".knob").knob();
     });
     */
    
    
    
    /* ****************************************** */
    /* JS for UI Tooltip */
    /* ****************************************** */
    //$('.ui-tooltip').tooltip();
    
    
    
    /* ****************** */
    /* Date Time Picker JS */
    /* ****************** */
    $('.datetime_picker').datetimepicker({
        format : "DD.MM.YYYY HH:mm",
        calendarWeeks : true,
        locale : "de",
        sideBySide : true,
        defaultDate : moment().hour(8).minute(0).second(0),
    });
    
    $('.date_picker').datetimepicker({
        format : "DD.MM.YYYY",
        calendarWeeks : true,
        locale : "de",
        defaultDate : moment()
    });

    
    $('.time_picker').datetimepicker({
        format : "HH:mm",
        calendarWeeks : true,
       // locale : "de",
       // defaultDate : moment()
    });
    
    /* ****************************************** */
    /* On Off Switches */
    /* ****************************************** */
    $('.on-off-switch').onoff();
});

