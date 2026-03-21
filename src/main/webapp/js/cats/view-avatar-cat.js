function  previewImage(input){
    const img = document.getElementById('viewImg');
    const file = input.files[0];
    if (file) {
        img.src = URL.createObjectURL(file);
    }
}

