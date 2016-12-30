//
// Created by vadimcg on 13.12.16.
//

#include "FileCryptor.h"


int FileCryptor::getSize() const{
    return this->size_;
}

std::string FileCryptor::getName() const{
    return this->name_;
}

std::string FileCryptor::getFileNameFromPath(const std::string& filePath) {

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


bool FileCryptor::encryptFile(std::string& path,std::string& code){

    this->name_= this->getFileNameFromPath(path);
    this->size_=0;
    this->path_=path.substr(0,path.size()-this->name_.size());
    this->code_=this->hashCode(code);

    std::ifstream originalFile;
    std::ofstream encryptFile(this->encryptedFilePath(),std::ios::binary);

    originalFile.open (this->originalFilePath(),std::ios::binary);

    if(originalFile.is_open()){

        if(encryptFile.is_open()){

        }else{
            std::cout<<"Fail, while  creating cypted file!"<< std::endl;
            return false;
        }

        std::cout<<"File was opened successful!"<< std::endl;

        /*unsigned int key_schedule[AES_BLOCK_SIZE * 4] = { 0 };
        aes_key_setup(this->code_,key_schedule,AES_KEY_SIZE);
        unsigned char *encryptData=new unsigned char[AES_BLOCK_SIZE];

        do {
            int count=fread(encryptData, AES_BLOCK_SIZE, 8, originalFile);




        }while(true);*/




        //unsigned char* decryptData=new unsigned char[AES_BLOCK_SIZE];

        //aes_encrypt(input, encryptData, key_schedule, AES_KEY_SIZE);
        //aes_decrypt(encryptData,decryptData,key_schedule,AES_KEY_SIZE);

        /*char* value=new char[1];
        peFileStream.read(value,1);*/

        return true;
    } else{

        std::cout<<"Fail, while  openning original file!"<< std::endl;
        return false;

    }
}



unsigned char* FileCryptor::hashCode(std::string &userCode){
    
    size_t int_val=std::hash<std::string>()(userCode);
    std::string strHash=to_string(int_val);

    char const * hash=strHash.c_str();
    unsigned char aes_key[strHash.size()+1];

    memcpy(aes_key,hash,strHash.size());
    aes_key[strHash.size()]=0x00;

    return aes_key;
}


std::string FileCryptor::getCryptedFileName(){
    return ANNEX+this->name_ ;
}

const char* FileCryptor::originalFilePath(){
    return (this->path_+this->name_).c_str();
}

const char* FileCryptor::encryptedFilePath(){
    return (this->path_+this->getCryptedFileName()).c_str();
}



