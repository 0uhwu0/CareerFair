function defaultSet() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    $("#startDate").attr("min", formattedDate);
    $("#endDate").attr("min", formattedDate);
    $("#startDate").attr("value", formattedDate);
    $("#endDate").attr("value", formattedDate);

    optionSet();

}

function optionSet(){
    const companyId = $("#companyName").children("option:selected").data('companyId');
    const round = $("#companyName").children("option:selected").data('round');
    const industryId = $("#companyName").children("option:selected").data('industryId');
    const address = $("#companyName").children("option:selected").data('address');

    $("#companyId").val(companyId);
    $("#round").val(round);
    $("#address").val(address);

    $("#industryId option").each(function () {
        if ($(this).val() == industryId) {
            $(this).prop("selected", true);
        } else {
            $(this).prop("selected", false);
        }
    })

}
$("#startDate").on("change", function () {
    const startDate = $(this).val();
    $("#endDate").attr("min", startDate);
});


$("#companyName").on("change", function () {
    optionSet();


})

$("#salary").on("keyup", function () {
    const length = $(this).val().length;
    let salary = $(this).val();

    let first = salary.substring(0, length - 8);
    if (first != '') {
        first += '억';
    }

    let second = salary.substring(length - 8, length - 4);
    second = modifyNumber(second);
    if (second != '') {
        second += '만';
    }

    let last = salary.substring(length - 4);
    last = modifyNumber(last);
    last += '원';

    salary = first + second + last;

    $("#salaryDetail").text(salary);

})

function modifyNumber(number) {
    const length = number.length;
    for (let i = 0; i < length; i++) {
        if (number[i] != '0') {
            return number = number.substring(i);
            break;
        } else {

        }
    }

    return '';

}
