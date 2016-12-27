#import <algorithm>
#import <string>
#import <fstream>
#import <iostream>
#import "aes/aes.h"
#include <sstream>

#ifndef NDKEXPERIMENTS_FILEINSTANCE_H
#define NDKEXPERIMENTS_FILEINSTANCE_H


class FileInstance {

private:

    const int AES_KEY_SIZE=256;

    int size_;

    std::string name_;
    std::string path_;

    unsigned char* code_;

    /**
     * Function return name and type of file
     */
    std::string getFileNameFromPath(const std::string& filePath);


    unsigned char* hashCode(std::string &userCode);

    template <typename T>
    std::string to_string(T value) {
        std::ostringstream os ;
        os << value ;
        return os.str() ;
    }


public:

    FileInstance(std::string& path_,std::string& code_);
    FileInstance(const FileInstance& value);
    FileInstance(FileInstance&& value);

    ~FileInstance();

    int getSize() const;
    std::string getName() const;

    bool encryptFile();

};


#endif //NDKEXPERIMENTS_FILEINSTANCE_H
