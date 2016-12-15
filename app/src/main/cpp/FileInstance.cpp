//
// Created by vadimcg on 13.12.16.
//

#include "FileInstance.h"


FileInstance::FileInstance(std::string& path_){
    this->name_= this->getFileNameFromPath(path_);
    this->size_=0;
    this->path_=path_;
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


int FileInstance::getSize(){
    return this->size_;
}

std::string FileInstance::getName(){
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