#!/bin/bash

systemctl status lvm2-lvmetad.service

disk="/dev/sdx"
disk_size="300GB"
disk_no="1"
parted -s "${disk}" mklabel gpt
parted -s "${disk}" mkpart primary 0 ${disk_size}
parted -s "${disk}" toggle ${disk_no} lvm

partition="${disk}${disk_no}"
vg_name="vg_rhel"
lv_name="data"
lv_size="100GB"
pvcreate "${partition}" && pvscan && pvdisplay "${partition}"
vgcreate "${vg_name}" "${partition}" && vgscan && vgdisplay "${vg_name}"
# vgextend "${vg_name}" "${another_partition}"
lvcreate --name "${lv_name}" --size "${lv_size}" "${vg_name}" && lvscan && lvdisplay "${vg_name}/${lv_name}"
