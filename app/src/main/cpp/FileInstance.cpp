//
// Created by vadimcg on 13.12.16.
//

#include "FileInstance.h"


FileInstance::FileInstance(std::string& path,std::string& code){
    this->name_= this->getFileNameFromPath(path);
    this->size_=0;
    this->path_=path.substr(0,path.size()-this->name_.size());
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
    std::ifstream encryptFile;
    peFileStream.open (this->path_.c_str(),std::ios::binary);


    if(peFileStream.is_open()){



        std::cout<<"File was opened successful!"<< std::endl;

        unsigned int key_schedule[AES_BLOCK_SIZE * 4] = { 0 };
        aes_key_setup(this->code_,key_schedule,AES_KEY_SIZE);
        unsigned char *encryptData=new unsigned char[AES_BLOCK_SIZE];

        do {
            int count=fread(encryptData, AES_BLOCK_SIZE, 8, peFileStream);




        }while(true);




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
    size_t int_val=std::hash<std::string>()(userCode);
    std::string strHash=to_string(int_val);

    char const * hash=strHash.c_str();
    unsigned char aes_key[strHash.size()+1];

    memcpy(aes_key,hash,strHash.size());
    aes_key[strHash.size()]=0x00;

    return aes_key;
}
