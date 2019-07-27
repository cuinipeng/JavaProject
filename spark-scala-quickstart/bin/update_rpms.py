#!/usr/bin/env python
import os
import sys
from xml.etree import ElementTree as ET

DEFAULT_PASSWD = '#defaultpasswd$'

def gen_passwd_hash(passwd=None):
    hash = None
    if os.name == 'nt':
        print('Not support windows to generate passwd hash')
        return hash
    elif os.name == 'posix':
        import crypt

    # [<crypt.METHOD_SHA512>, <crypt.METHOD_SHA256>, <crypt.METHOD_MD5>, <crypt.METHOD_CRYPT>]
    # print(crypt.methods)

    # '$6$Oya193B0M4bZYxgN'
    salt = crypt.mksalt(method=crypt.METHOD_SHA512)
    hash = crypt.crypt(passwd, salt)

    return hash


def pre_check():
    selfname = sys.argv[0]
    fullpath = os.path.realpath(selfname)
    rpm_xml = os.path.join(os.path.dirname(os.path.dirname(fullpath)), 'conf', 'rpm.xml')
    print(rpm_xml)

    if not os.path.exists(rpm_xml):
        print("{0} is missing".format(rpm_xml))
        return None

    return rpm_xml


def read_xml(filename=None):
    #TODO
    tree = ET.parse(filename)
    root_node = tree.getroot()

    # print('Elem: {0}'.format(root_node))
    # print('Tag: {0}'.format(root_node.tag))
    # print('Attrib: {0}'.format(root_node.attrib))
    # print('Text: {0}'.format(root_node.text))

    for first_level_node in root_node:
        print('Elem: {0}, Tag: {1}, Attrib: {2}, Text: {3}'.format(
                first_level_node, first_level_node.tag,
                first_level_node.attrib, first_level_node.text))
        for second_level_node in first_level_node:
            print('Elem: {0}, Tag: {1}, Attrib: {2}, Text: {3}'.format(
                            second_level_node, second_level_node.tag,
                            second_level_node.attrib, second_level_node.text))

    # 查找全部的 country 节点
    country_nodes = root_node.findall('country')
    print(country_nodes)

    # 删除属性 name="Singapore" 的节点
    for country_node in country_nodes:
        if country_node.attrib['name'] == 'Singapore':
            root_node.remove(country_node)

    # 按路径查找节点
    neighbor_nodes = root_node.findall('country/neighbor')
    print(neighbor_nodes)

    # 创建一个新节点
    tag_name = 'neighbor'
    attrib = {'name': 'China', 'direction': 'S'}
    elem = ET.Element('neighbor', attrib)
    country_node.append(elem)
    # 添加后需要格式化
    # $ xmllint --format rpm.xml

    return tree

def write_xml(filename=None, tree=None):
    #TODO
    tree.write(filename, encoding='utf-8', xml_declaration=True)


def main():
    gen_passwd_hash()
    filename = pre_check()
    tree = read_xml(filename)
    prefix, suffix = os.path.splitext(filename)
    out = prefix + "_tmp" + suffix
    write_xml(out, tree)


if __name__ == '__main__':
    main()
