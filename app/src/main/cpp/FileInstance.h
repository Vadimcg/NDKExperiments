#import <algorithm>
#import <string>
#import <fstream>
#import <iostream>
#import "aes/aes.h"

#ifndef NDKEXPERIMENTS_FILEINSTANCE_H
#define NDKEXPERIMENTS_FILEINSTANCE_H


class FileInstance {

private:

    int size_;
    std::string name_;
    std::string path_;

    /**
     * Function return name and type of file
     */
    std::string getFileNameFromPath(const std::string& filePath);

public:

    FileInstance(std::string& path_);
    FileInstance(const FileInstance& value);
    FileInstance(FileInstance&& value);

    ~FileInstance();

    int getSize() const;
    std::string getName() const;

    bool encryptFile();

};


#endif //NDKEXPERIMENTS_FILEINSTANCE_H
