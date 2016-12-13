//
// Created by vadimcg on 13.12.16.
//

#include "FileInstance.h"


FileInstance::FileInstance(std::string& path_){
    this->name_= nullptr;
    this->size_=0;
    this->path_=0;
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
