//
// Created by vadimcg on 13.12.16.
//

#include "FileInstance.h"


FileInstance::FileInstance(std::string& path_,std::string& code){
    this->name_= this->getFileNameFromPath(path_);
    this->size_=0;
    this->path_=path_;
    this->code_=this->hashCode(code);

}

FileInstance::FileInstance(const FileInstance& value){
    this->name_=value.name_;
    this->size_=value.size_;
    this->path_=value.path_;
}

FileInstance::FileInstance(FileInstance&& value){
    this->name_=value.name_;
    this->size_=value.size_;
    this->path_=value.path_;
}


FileInstance::~FileInstance(){
}


int FileInstance::getSize() const{
    return this->size_;
}

std::string FileInstance::getName() const{
    return this->name_;
}

std::string FileInstance::getFileNameFromPath(const std::string& filePath) {

    std::string fileName;

    std::string::const_reverse_iterator it = std::find(filePath.rbegin(), filePath.rend(), '/');

    if (it != filePath.rend()) {
        fileName.assign(filePath.rbegin(), it);
        std::reverse(fileName.begin(), fileName.end());
        return fileName;
    }
    else
        return fileName;
}


bool FileInstance::encryptFile(){

    std::ifstream peFileStream;
    peFileStream.open (this->path_.c_str(),std::ios::binary);


    if(peFileStream.is_open()){

        std::cout<<"File was opened successful!"<< std::endl;

        unsigned int key_schedule[AES_BLOCK_SIZE * 4] = { 0 };
        aes_key_setup(this->code_,key_schedule,AES_KEY_SIZE);


        unsigned char input[]={'v','a','d','i','m','\0'};

        unsigned char *encryptData=new unsigned char[AES_BLOCK_SIZE];
        unsigned char* decryptData=new unsigned char[AES_BLOCK_SIZE];

        aes_encrypt(input, encryptData, key_schedule, AES_KEY_SIZE);
        aes_decrypt(encryptData,decryptData,key_schedule,AES_KEY_SIZE);

        /*char* value=new char[1];
        peFileStream.read(value,1);*/

        return true;
    } else{

        std::cout<<"Fail, while  openning file!"<< std::endl;
        return false;

    }
}



unsigned char* FileInstance::hashCode(std::string &userCode){

    unsigned char aes_key[32]= { 0x00, 0x00, 0x00, 0x10, 0x15, 0xca, 0x71,
                                 0xbe, 0x2b, 0x73, 0xae, 0xf0, 0x85, 0x7d, 0x77, 0x81, 0x1f, 0x35, 0x2c,
                                 0x07, 0x3b, 0x61, 0x08, 0xd7, 0x2d, 0x98, 0x10, 0xa3, 0x09, 0x14, 0xdf,
                                 0xf4 };

    std::hash<std::string> hash_fn;
    size_t int_val=hash_fn(userCode);

    int n=0;
    do{
        aes_key[n]=int_val>>8;
        n++;
    }while(n*8<sizeof(unsigned int));

    return;
}
