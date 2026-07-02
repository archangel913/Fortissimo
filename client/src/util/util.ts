export function convertSize(number: string, magnification: number): string {
  const match = number.match(/^([\d.]+)([a-z%]+)$/i);

  if (match && match[1] && match[2]) {
    const num = parseFloat(match[1]);
    const unit = match[2];

    return num * magnification + unit;
  } else {
    return "0px";
  }
}

export function base64ToBlob(base64: string, mimeType: string = 'image/jpeg'):Blob | null{
  const finalBase64Data = base64.includes(',') ? base64.split(',')[1] : base64;
  if(!finalBase64Data){
    return null;
  }
  const byteCharacters = atob(finalBase64Data);
  const byteNumbers = new Array(byteCharacters.length);
  for (let i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  const byteArray = new Uint8Array(byteNumbers);
  return new Blob([byteArray], { type: mimeType });
}
