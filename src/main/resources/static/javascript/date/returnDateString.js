export default function returnDateString(date){
    const resDate = new Date(date);
    const resMonth = resDate.getMonth() + 1;
    const strMonth =  resMonth < 10 ? '0' + resMonth : resMonth;

    const resDay = resDate.getDate();
    const strDate = resDay < 10 ? '0' + resDay : resDay;
	
	return `${resDate.getFullYear()}-${strMonth}-${strDate}`;
}